package za.co.knonchalant.candogram;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;

public class ConsoleBot {

    public static final String TOKEN = "NzczOTI0OTAwMTM3NTk5MDA3.X6QTuw.JjI3Zj4NrgRAYKQWr0RhbX1tK4Y";

    public static void main(String[] args) throws LoginException {
        JDABuilder.createLight(TOKEN, GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES)
                .addEventListeners(new PingReply())
                .setActivity(Activity.playing("Type !ping"))
                .build();

    }
}
