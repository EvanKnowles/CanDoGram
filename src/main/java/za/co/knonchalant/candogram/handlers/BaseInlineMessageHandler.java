package za.co.knonchalant.candogram.handlers;

import za.co.knonchalant.candogram.api.IBotAPI;

public abstract class BaseInlineMessageHandler extends BaseMessage implements IInlineHandler {
    public BaseInlineMessageHandler(String botName, IBotAPI bot) {
        super(botName, bot);
    }
}
