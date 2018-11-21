package za.co.knonchalant.candogram;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.*;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;
import okhttp3.OkHttpClient;
import za.co.knonchalant.candogram.handlers.IInlineHandler;
import za.co.knonchalant.candogram.handlers.IMessageHandler;
import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.candogram.handlers.User;
import za.co.knonchalant.candogram.handlers.update.TelegramUpdate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by evan on 2016/04/08.
 */
public class TelegramBotAPI implements IBotAPI {
    private TelegramBot bot;
    private List<IMessageHandler> iMessageHandlers = new ArrayList<>();
    private int offset;
    private IInlineHandler inlineHandler;
    private String name;

    public TelegramBotAPI(String name, String apiKey) {
        this.name = name;
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();

        TelegramBot bot = TelegramBotAdapter.buildCustom(name, client);
        this.bot = TelegramBotAdapter.build(apiKey);
        bot.execute(new GetMe());
    }

    @Override
    public String getName() {
        return name;
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
    public List<IUpdate> getUpdates(Integer limit) {

        GetUpdatesResponse updates = bot.execute(new GetUpdates().limit(limit).offset(offset).timeout(0));

        List<IUpdate> result = new ArrayList<>();

        for (Update update : updates.updates()) {
            if (update.inlineQuery() != null) {
                System.out.println("Inline: " + update.inlineQuery().query());
            }
            result.add(new TelegramUpdate(update));
        }

        return result;
    }

    @Override
    public void sendMessage(IUpdate message, String text, Object... args) {
        sendTheMessage(new SendMessage(message.getChatId(), String.format(text, args)).parseMode(ParseMode.Markdown));
    }


    @Override
    public void sendMessage(IUpdate message, String text) {
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

    private void sendTheMessage(SendMessage sendMessage) {
        SendResponse execute = bot.execute(sendMessage);
        if (!execute.isOk()) {
            System.out.println("Sending message failed: " + execute.errorCode() + " - " + execute.description());
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

    protected Keyboard defaultKeyboard(List<List<String>> keyboardList) {
        return new ReplyKeyboardMarkup(getArrayFromKeyboard(keyboardList)).oneTimeKeyboard(true)
                .resizeKeyboard(true)
                .selective(true);
    }


    private String[][] getArrayFromKeyboard(List<List<String>> keyboardList) {
        String[][] array = new String[keyboardList.size()][];

        int j = 0;
        for (List<String> nestedList : keyboardList) {
            array[j++] = nestedList.toArray(new String[nestedList.size()]);
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
    public void sendPhoto(String chatId, byte[] photoUrl) {
        getBot().execute(new SendPhoto(chatId, photoUrl));
    }
}
