package za.co.knonchalant.candogram;

import za.co.knonchalant.candogram.api.DiscordBotAPI;
import za.co.knonchalant.candogram.api.IBotAPI;
import za.co.knonchalant.candogram.api.update.DiscordUpdate;
import za.co.knonchalant.candogram.handlers.IMessageHandler;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LaunchCanDoGram {
    public static final String TOKEN = "NzczOTI0OTAwMTM3NTk5MDA3.X6QTuw.JjI3Zj4NrgRAYKQWr0RhbX1tK4Y";
    public static final String NAME = "SpamBot";


    public static void main(String[] args) throws LoginException {
        HandlerBot handlerBot = new HandlerBot(new NoOpDAO());
        PollBot pollBot = new PollBot(handlerBot);

        pollBot.start(buildHelloBot());
    }

    private static List<Bots> buildHelloBot() throws LoginException {
        List<IMessageHandler > handlers = new ArrayList<>();
        IBotAPI<DiscordUpdate> botAPI;
        botAPI = new DiscordBotAPI(TOKEN);
        String botName = NAME;

        // handlers are conveniently easily interchangeable between bots
        handlers.add(new HelloHandler(botName, botAPI));
        handlers.add(new RouletteHandler(botName, botAPI));
        handlers.add(new EightBallHandler(botName, botAPI));

        return Collections.singletonList(new Bots(NAME, Collections.singletonList(botAPI), handlers));
    }

}


