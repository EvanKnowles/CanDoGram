package za.co.knonchalant.candogram;

import za.co.knonchalant.candogram.handlers.IUpdate;

import javax.ejb.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
@Lock(LockType.READ)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class PollBot implements ShutdownNotify, IBot {
    private static final Logger LOGGER = Logger.getLogger(PollBot.class.getName());

    private volatile boolean running = true;

    @EJB
    HandlerBot handlerBot;

    private List<Bots> bots;

    public PollBot(HandlerBot handlerBot) {
        this.handlerBot = handlerBot;
    }

    public PollBot() {


    }

    @Asynchronous
    public void start(List<Bots> bots) {
        this.bots = bots;
        while (running) {
            for (Bots bot : bots) {
                if (bot != null) {
                    processBot(bot);
                }
            }

            sleep();
        }
        LOGGER.info("Received a shutdown, shutting down.");
    }

    @Override
    public void shutdown() {
        notifyShutdown();
    }

    private void processBot(Bots bots) {
        try {
            for (IBotAPI bot : bots.getApis()) {

                if (bot.supportsUpdateListener()) {
                    bot.registerUpdateListener(updates -> {
                        process(bot, updates);
                    });
                } else {
                    Collection<IUpdate> updates = onlyLast(bot.getUpdates(100));

                    process(bot, updates);
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Update exception", ex);
        }
    }

    private void process(IBotAPI bot, Collection<IUpdate> updates) {
        for (IUpdate update : updates) {
            bot.setOffset((int) (update.getId() + 1));
            handlerBot.handle(bot, update);
        }
    }

    private Collection<IUpdate> onlyLast(List<IUpdate> updates) {
        Map<Long, IUpdate> mostRecent = new HashMap<>();
        for (IUpdate update : updates) {
            if (update.skip()) {
                continue;
            }

            if (update.getUser() == null || update.getUser) {
                System.out.println("Update has no user! " + update);
                continue;
            } 
            mostRecent.put(update.getUser().getId(), update);
        }

        return mostRecent.values();
    }

    public Bots find(String name) {
        for (Bots bot : bots) {
            if (name.equals(bot.getName())) {
                return bot;
            }
        }
        System.out.println("Couldn't find " + name + " in " + bots);
        return null;
    }

    private void sleep() {
        try {
            Thread.sleep(100);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void notifyShutdown() {
        LOGGER.info("Telegram server is shutting down.");
        running = false;

        for (Bots bot : bots) {
            for (IBotAPI api : bot.getApis()) {
                api.unregisterUpdateListener();
            }

        }
    }
}
