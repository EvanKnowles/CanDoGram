package za.co.knonchalant.candogram;

import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ParseMode;
import za.co.knonchalant.candogram.handlers.*;

import java.util.List;

public interface IBotAPI {
    List<IMessageHandler> getHandlers();

    void setInlineHandler(IInlineHandler handler);

    IInlineHandler getInlineHandler();

    void addHandler(IMessageHandler handler);

    void addHandlers(List<IMessageHandler> handler);

    List<IUpdate> getUpdates(Integer limit);

    void sendMessage(IUpdate message, String text, Object... args);

    void sendMessage(IUpdate message, String text);

    void sendMessageWithKeyboard(IUpdate update, List<List<String>> keyboardList, String text);

    boolean typing(IUpdate update);

    void sendMessage(Long chatId, String message, ParseMode parseMode, boolean disableWebPagePreview, Integer messageId, Keyboard keyboard);

    void updateMessage(Long chatId, String message, Integer messageId, InlineKeyboardMarkup keyboard);

    void setOffset(int updateId);

    List<User> getChatUsers(long chatId);

    void sendInlinePhoto(String inlineId, String photoUrl, String thumbnailUrl, int width, int height);

    void sendPhoto(String chatId, byte[] photoUrl);

    void sendAnimation(String chatId, byte[] photoUrl);

    String getName();

    boolean supportsUpdateListener();

    void registerUpdateListener(IBotUpdatesHandler handler);

    void unregisterUpdateListener();
}
