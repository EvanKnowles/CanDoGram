package za.co.knonchalant.candogram;

import za.co.knonchalant.candogram.api.IBotAPI;
import za.co.knonchalant.candogram.domain.PendingResponse;
import za.co.knonchalant.candogram.handlers.BaseMessageHandler;
import za.co.knonchalant.candogram.handlers.IUpdate;


public class HelloHandler extends BaseMessageHandler {

    public HelloHandler(String botName, IBotAPI bot) {
        super(botName, "hello", bot, true);
    }

    @Override
    public PendingResponse handle(IUpdate update) {
        typing(update);

        sendMessage(update, "Hello, " + update.getUser().getUserName() + " - have a cat!");
        getBot().sendPhoto(update, FileHandler.fetchRemoteFile("https://cataas.com/cat"));

        // we're not expecting any response messages, so return null
        return null;
    }
}