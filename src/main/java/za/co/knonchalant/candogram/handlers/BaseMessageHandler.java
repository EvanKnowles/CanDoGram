package za.co.knonchalant.candogram.handlers;

import za.co.knonchalant.candogram.api.IBotAPI;

/**
 * Created by evan on 2016/03/11.
 */
public abstract class BaseMessageHandler extends BaseMessage implements IMessageHandler {
    String command;

    public BaseMessageHandler(String botName, String command, IBotAPI bot) {
        super(botName, bot);
        this.command = command;
    }

    public BaseMessageHandler(String botName, String command, IBotAPI bot, boolean noargs) {
        super(botName, bot, noargs);
        this.command = command;
    }

    protected String getKeywords(String text) {
        return super.getKeywords(text, getCommand());
    }

    @Override
    public boolean matches(IUpdate update) {
        if (update == null || update.getText() == null) {
            return false;
        }

        String text = update.getText();

        return getKeywords(text, command) != null;
    }

    @Override
    public String getCommand() {
        return command;
    }

    @Override
    public String getDescription() {
        return null;
    }
}
