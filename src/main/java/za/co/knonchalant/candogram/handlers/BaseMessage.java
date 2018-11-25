package za.co.knonchalant.candogram.handlers;

import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import za.co.knonchalant.candogram.IBotAPI;
import za.co.knonchalant.candogram.handlers.exception.CouldNotLookupBeanException;

import javax.naming.InitialContext;
import java.util.List;

/**
 * Created by evan on 2016/03/07.
 */
public abstract class BaseMessage {
    private static final String COMMAND_WITH_BOT = "/%s@%s ";
    private static final String COMMAND = "/%s ";
    private static final String NO_ARGS_COMMAND = "/%s";

    private String botName;
    private IBotAPI bot;
    private boolean noargs;

    public BaseMessage() {
    }

    public BaseMessage(String botName, IBotAPI bot) {
        this.botName = botName;
        this.bot = bot;
    }

    public BaseMessage(String botName, IBotAPI bot, boolean noargs) {
        this.botName = botName;
        this.bot = bot;
        this.noargs = noargs;
    }

    protected boolean typing(IUpdate update) {
        return getBot().typing(update);
    }

    protected IBotAPI getBot() {
        return bot;
    }

    protected void sendMessage(IUpdate message, String text, Object... args) {
        bot.sendMessage(message, text, args);
    }

    protected void sendMessage(IUpdate message, String text) {
        bot.sendMessage(message, text);
    }

    protected void sendMessageWithKeyboard(IUpdate update, List<List<String>> keyboardList, String text) {
        getBot().sendMessageWithKeyboard(update, keyboardList, text);
    }

    private String matchCommand(String text, String command) {
        String theCommand = noargs ? NO_ARGS_COMMAND : COMMAND;
        String actual = String.format(theCommand, command);

        boolean matchBasic = text.startsWith(actual) && (text.length() >= actual.length());
        if (matchBasic) {
            return text.substring(actual.length());
        }

        actual = String.format(COMMAND_WITH_BOT, command, botName);
        if (text.startsWith(actual) && (text.length() > actual.length())) {
            return text.substring(actual.length());
        }
        return null;
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
