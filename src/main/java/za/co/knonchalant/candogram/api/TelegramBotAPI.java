package za.co.knonchalant.candogram.api;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.*;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;
import okhttp3.OkHttpClient;
import za.co.knonchalant.candogram.ChatBacklog;
import za.co.knonchalant.candogram.IBotAPI;
import za.co.knonchalant.candogram.handlers.*;
import za.co.knonchalant.candogram.handlers.update.TelegramUpdate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by evan on 2016/04/08.
 */
public class TelegramBotAPI implements IBotAPI<TelegramUpdate> {
    private TelegramBot bot;
    private List<IMessageHandler> iMessageHandlers = new ArrayList<>();
    private int offset;
    private IInlineHandler inlineHandler;
    private String name;
    private boolean throttle;

    private boolean registeredForUpdates;

    private final Map<Long, ChatBacklog> backlog = new HashMap<>();

    public TelegramBotAPI(String name, String apiKey, boolean throttle) {
        this.name = name;
        this.throttle = throttle;

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .build();

        this.bot = new TelegramBot.Builder(apiKey).okHttpClient(client).build();
        bot.execute(new GetMe());
    }

    public TelegramBotAPI(String name, String apiKey) {
        this(name, apiKey, true);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean supportsUpdateListener() {
        return false;
    }

    @Override
    public void registerUpdateListener(IBotUpdatesHandler handler) {
        if (registeredForUpdates) {
            return;
        }

        registeredForUpdates = true;

        bot.setUpdatesListener(updates -> {
            handler.handle(updates.stream().map(TelegramUpdate::new).collect(Collectors.toList()));

            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    @Override
    public void unregisterUpdateListener() {
        registeredForUpdates = false;
        bot.removeGetUpdatesListener();
    }

    private TelegramBot getBot() {
        return bot;
    }

    @Override
    public List<IMessageHandler> getHandlers() {
        return iMessageHandlers;
    }

    @Override
    public void setInlineHandler(IInlineHandler handler) {
        this.inlineHandler = handler;
    }

    @Override
    public IInlineHandler getInlineHandler() {
        return inlineHandler;
    }

    @Override
    public void addHandler(IMessageHandler handler) {
        iMessageHandlers.add(handler);
    }

    @Override
    public void addHandlers(List<IMessageHandler> handler) {
        for (IMessageHandler iMessageHandler : handler) {
            addHandler(iMessageHandler);
        }
    }

    @Override
    public List<TelegramUpdate> getUpdates(Integer limit) {

        GetUpdatesResponse updates = bot.execute(new GetUpdates().limit(limit).offset(offset).timeout(0));

        List<TelegramUpdate> result = new ArrayList<>();
        if (updates == null || updates.updates() == null) {
            return result;
        }

        for (Update update : updates.updates()) {
            if (update.inlineQuery() != null) {
                System.out.println("Inline: " + update.inlineQuery().query());
            }
            result.add(new TelegramUpdate(update));
        }

        return result;
    }

    @Override
    public void sendMessage(TelegramUpdate message, String text, Object... args) {
        sendTheMessage(new SendMessage(message.getChatId(), String.format(text, args)).parseMode(ParseMode.Markdown));
    }


    @Override
    public void sendMessage(TelegramUpdate message, String text) {
        sendTheMessage(new SendMessage(message.getChatId(), text).parseMode(ParseMode.Markdown));
    }

    @Override
    public void sendMessage(Long chatId, String message, ParseMode parseMode, boolean disableWebPagePreview, Integer messageId, Keyboard keyboard) {

        if (parseMode == null) {
            parseMode = ParseMode.Markdown;
        }

        SendMessage sendMessage = new SendMessage(chatId, message)
                .parseMode(parseMode)
                .disableWebPagePreview(disableWebPagePreview);

        if (keyboard != null) {
            sendMessage = sendMessage.replyMarkup(keyboard);
        }

        if (messageId != null) {
            sendMessage = sendMessage
                    .replyToMessageId(messageId);
        }

        sendTheMessage(sendMessage);
    }

    @Override
    public void updateMessage(Long chatId, String message, Integer messageId, InlineKeyboardMarkup keyboard) {
        EditMessageReplyMarkup sendMessage = new EditMessageReplyMarkup(chatId, messageId);
        if (keyboard != null) {
            sendMessage = sendMessage.replyMarkup(keyboard);
        }

        BaseResponse execute = bot.execute(sendMessage);
        if (!execute.isOk()) {
            System.out.println("Sending message failed: " + execute.errorCode() + " - " + execute.description());
        }
    }

    @Override
    public void deleteMessage(long chatId, int messageId) {
        DeleteMessage deleteMessage = new DeleteMessage(chatId, messageId);
        BaseResponse execute = bot.execute(deleteMessage);
        if (!execute.isOk()) {
            System.out.println("Deleting message failed: " + execute.errorCode() + " - " + execute.description());
        }
    }

    private void sendTheMessage(SendMessage sendMessage) {
        Object chat_idObj = sendMessage.getParameters().get("chat_id");
        Long chatId = Long.parseLong(chat_idObj.toString());

        if (!backlog.containsKey(chatId)) {
            synchronized (backlog) {
                if (!backlog.containsKey(chatId)) {
                    backlog.put(chatId, new ChatBacklog(throttle));
                }
            }
        }

        ChatBacklog sendMessages = backlog.get(chatId);

        // IntelliJ is freaking out, but I think it's fine
        synchronized (sendMessages) {
            sendMessages.add(sendMessage);
        }

        sendBacklog(chatId);
    }

    private void sendOneMessage(SendMessage sendMessage) {
        boolean sent = false;
        SendResponse execute = null;
        while (!sent) {
            try {
                execute = bot.execute(sendMessage);
                sent = true;
            } catch (Exception ex) {

                // seems like there's nothing we can do. if we try re-send the message, we may just get the same message sent again.
                // we'll try just not break the process
                return;
            }
        }

        if (!execute.isOk()) {
            System.out.println("Sending message failed: " + execute.errorCode() + " - " + execute.description());
            return;
        }

        execute.message();
    }

    private void sendBacklog(Long chatId) {
        List<SendMessage> thisLog;
        ChatBacklog sendMessages = backlog.get(chatId);

        if (!backlog.containsKey(chatId) || sendMessages.isEmpty()) {
            return;
        }

        // IntelliJ is freaking out, but I think it's fine
        synchronized (sendMessages) {
            thisLog = new ArrayList<>(sendMessages.getMessages());
            sendMessages.clear();
        }

        synchronized (sendMessages.getSentLock()) {
            for (SendMessage sendMessage : thisLog) {
                long timeToWait = sendMessages.timeLeft();
                if (timeToWait > 0) {
                    sleep(timeToWait);
                }

                sendMessages.reset();

                sendOneMessage(sendMessage);
            }
        }
    }

    private void sleep(long timeToWait) {
        try {
            Thread.sleep(timeToWait);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMessageWithKeyboard(IUpdate message, List<List<String>> keyboardList, String text) {
        SendMessage sendMessage = new SendMessage(message.getChatId(), text)
                .parseMode(ParseMode.Markdown)
                .replyMarkup(defaultKeyboard(keyboardList))
                .replyToMessageId((int) message.getId());

        sendTheMessage(sendMessage);
    }

    private Keyboard defaultKeyboard(List<List<String>> keyboardList) {
        return new ReplyKeyboardMarkup(getArrayFromKeyboard(keyboardList)).oneTimeKeyboard(true)
                .resizeKeyboard(true)
                .selective(true);
    }


    private String[][] getArrayFromKeyboard(List<List<String>> keyboardList) {
        String[][] array = new String[keyboardList.size()][];

        int j = 0;
        for (List<String> nestedList : keyboardList) {
            array[j++] = nestedList.toArray(new String[0]);
        }
        return array;
    }


    @Override
    public boolean typing(IUpdate update) {
        BaseResponse execute = getBot().execute(new SendChatAction(update.getChatId(), ChatAction.typing.name()));
        return execute.isOk();
    }


    @Override
    public void setOffset(int updateId) {
        this.offset = Math.max(updateId, offset);
    }

    @Override
    public List<User> getChatUsers(long chatId) {
        throw new RuntimeException("Doesn't seem possible.");
    }

    @Override
    public void sendInlinePhoto(String inlineId, String photoUrl, String thumbnailUrl, int width, int height) {
        InlineQueryResultPhoto inlineQueryResultPhoto = new InlineQueryResultPhoto(inlineId, photoUrl, thumbnailUrl);
        inlineQueryResultPhoto.photoHeight(height);
        inlineQueryResultPhoto.photoWidth(width);

        BaseResponse execute = getBot().execute(new AnswerInlineQuery(inlineId, inlineQueryResultPhoto));
        System.out.println(execute);
    }

    @Override
    public void sendPhoto(TelegramUpdate chat, byte[] photoBytes) {
        getBot().execute(new SendPhoto(chat.getChatId(), photoBytes));
    }

    @Override
    public void sendAnimation(String chatId, byte[] documentBytes) {
        getBot().execute(new SendAnimation(chatId, documentBytes));
    }
}
