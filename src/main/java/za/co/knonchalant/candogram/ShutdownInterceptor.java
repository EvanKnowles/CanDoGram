package za.co.knonchalant.candogram;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by evan on 2016/03/07.
 */
public class ShutdownInterceptor {
    private static final Logger LOGGER = Logger.getLogger(ShutdownInterceptor.class.getName());

    private List<IBot> telegramBots = new ArrayList<>();

    public void addNotifier(IBot telegramBot) {
        this.telegramBots.add(telegramBot);
    }

    public void notifyShutdown() {
        LOGGER.warning(" **** SERVER SHUTTING DOWN ****");
        for (IBot telegramBot : telegramBots) {
            LOGGER.warning("** Notifying: " + telegramBot.toString());
            telegramBot.notifyShutdown();
        }

    }
}
