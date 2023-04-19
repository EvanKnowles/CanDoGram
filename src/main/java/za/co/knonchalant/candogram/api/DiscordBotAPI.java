package za.co.knonchalant.candogram.api;

import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ParseMode;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import za.co.knonchalant.candogram.api.update.DiscordUpdate;
import za.co.knonchalant.candogram.handlers.*;

import javax.security.auth.login.LoginException;
import java.util.List;

public class DiscordBotAPI extends BaseBotAPI<DiscordUpdate> {
    private final DiscordListenerAdapter discordListenerAdapter;
    private String token;

    public DiscordBotAPI(String token) throws LoginException {

        this.token = token;
        discordListenerAdapter = new DiscordListenerAdapter();

        JDABuilder.createLight(token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES)
                .addEventListeners(discordListenerAdapter)
                .setActivity(Activity.listening("to spamming"))
                .build();
    }

    @Override
    public List<DiscordUpdate> getUpdates(Integer limit) {
        return discordListenerAdapter.flush();
    }

    @Override
    public void sendMessage(DiscordUpdate message, String text, Object... args) {
        message.getChannel().sendMessage(String.format(text, args)).queue();
    }

    @Override
    public void sendMessage(DiscordUpdate message, String text) {
        message.getChannel().sendMessage(text).queue();
    }

    @Override
    public void deleteMessage(long chatId, int messageId) {
        throw new RuntimeException("deleteMessage not implemented");
    }

    @Override
    public void sendMessageWithKeyboard(IUpdate update, List<List<String>> keyboardList, String text) {

    }

    @Override
    public boolean typing(IUpdate update) {
        return false;
    }

    @Override
    public void sendMessage(Long chatId, String message, ParseMode parseMode, boolean disableWebPagePreview, Integer messageId, Keyboard keyboard) {

    }

    @Override
    public void updateMessage(Long chatId, String message, Integer messageId, InlineKeyboardMarkup keyboard) {

    }

    @Override
    public void setOffset(int updateId) {

    }

    @Override
    public List<User> getChatUsers(long chatId) {
        return null;
    }

    @Override
    public void sendInlinePhoto(String inlineId, String photoUrl, String thumbnailUrl, int width, int height) {

    }

    @Override
    public void sendPhoto(DiscordUpdate chatId, byte[] photoUrl) {
       chatId.getChannel().sendFile(photoUrl, "image.png").queue();
    }

    @Override
    public void sendAnimation(String chatId, byte[] photoUrl) {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean supportsUpdateListener() {
        return false;
    }

    @Override
    public void registerUpdateListener(IBotUpdatesHandler handler) {

    }

    @Override
    public void unregisterUpdateListener() {

    }
}
