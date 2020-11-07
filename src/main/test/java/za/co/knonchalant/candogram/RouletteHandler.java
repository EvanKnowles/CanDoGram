package za.co.knonchalant.candogram;

import za.co.knonchalant.candogram.api.IBotAPI;
import za.co.knonchalant.candogram.domain.PendingResponse;
import za.co.knonchalant.candogram.handlers.BaseMessageHandler;
import za.co.knonchalant.candogram.handlers.IUpdate;

public class RouletteHandler extends BaseMessageHandler {
    public RouletteHandler(String botName, IBotAPI<?> bot) {
        super(botName, "roulette", bot, true);
    }

    @Override
    public PendingResponse handle(IUpdate update) {
        sendMessage(update, "\\*_%s spins the chamber_\\*", update.getUser().getFirstName());

        getBot().typing(update);
        sleep(500);
        sendMessage(update, "\\*_whizz_\\*");
        sleep(500);
        sendMessage(update, "\\*_whizz-z-z-z barrel up to skull, finger..._\\*");
        sleep(1000);
        sendMessage(update, "\\*_snap_\\*");
        sleep(500);

        if (Math.random() * 60.0 <= 10.1) {
            sendMessage(update, "\\**BANG!*\\* " + update.getUser().getFirstName() + " is _dead_");
        } else {
            sendMessage(update, "\\*_click_\\* Nothing happens. " + update.getUser().getFirstName() + " looks relieved.");
        }

        return null;
    }

    private void sleep(int millis)  {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
