package za.co.knonchalant.candogram.handlers;

import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import za.co.knonchalant.candogram.IBotAPI;
import za.co.knonchalant.candogram.handlers.exception.CouldNotLookupBeanException;

import javax.naming.InitialContext;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by evan on 2016/03/07.
 */
public abstract class BaseMessage {
    private static final String ACTUAL_COMMAND = "/([^\\s@]+).*";
    private static final Pattern COMPILE = Pattern.compile(ACTUAL_COMMAND);

    private String botName;
    private IBotAPI<IUpdate> bot;
    private boolean noargs;

    public BaseMessage() {
    }

    public BaseMessage(String botName, IBotAPI<IUpdate> bot) {
        this.botName = botName;
        this.bot = bot;
    }

    public BaseMessage(String botName, IBotAPI<IUpdate> bot, boolean noargs) {
        this.botName = botName;
        this.bot = bot;
        this.noargs = noargs;
    }

    protected boolean typing(IUpdate update) {
        return getBot().typing(update);
    }

    public IBotAPI<IUpdate> getBot() {
        return bot;
    }

    protected void sendMessage(IUpdate message, String text, Object... args) {
        bot.sendMessage(message, text, args);
    }

    public void sendMessage(IUpdate message, String text) {
        bot.sendMessage(message, text);
    }

    protected void sendMessageWithKeyboard(IUpdate update, List<List<String>> keyboardList, String text) {
        getBot().sendMessageWithKeyboard(update, keyboardList, text);
    }

    protected void deleteMessage(IUpdate message) {
        int messageId = 0;
        try {
            messageId = Math.toIntExact(message.getMessageId());
        } catch (ArithmeticException e) {
            System.err.println("Unable to convert long messageId to int messageId without loss of precision");
            e.printStackTrace();
            return;
        }
        getBot().deleteMessage(message.getChatId(), messageId);
    }

    private String matchCommand(String text, String command) {
        if (!text.startsWith("/")) {
            return null;
        }

        Matcher matcher = COMPILE.matcher(text);
        if (!matcher.matches()) {
            return null;
        }

        String extractedCommand = matcher.group(1);
        if (!extractedCommand.equalsIgnoreCase(command)) {
            return null;
        }

        if (noargs) {
            return "";
        }

        if (!text.contains(" ")) {
            return "";
        }

        return text.substring(text.indexOf(" ")+1);
    }

    protected static Object lookupBean(String name) {
        try {
            InitialContext ic = new InitialContext();
            return ic.lookup(name);
        } catch (Exception ex) {
            throw new CouldNotLookupBeanException(ex);
        }
    }

    protected Keyboard locationKeyboard() {
        return new ReplyKeyboardMarkup(
                new KeyboardButton[]{
                        new KeyboardButton("Here I am").requestLocation(true)
                }
        ).oneTimeKeyboard(true)
                .resizeKeyboard(true)
                .selective(true);
    }


    protected Keyboard defaultKeyboard(List<List<String>> keyboardList) {
        return new ReplyKeyboardMarkup(getArrayFromKeyboard(keyboardList))
                .oneTimeKeyboard(true)
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

    protected String getKeywords(String text, String command) {
        return matchCommand(text, command);
    }

    public void setBot(IBotAPI bot) {
        this.bot = bot;
    }
}
