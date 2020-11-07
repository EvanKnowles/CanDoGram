package za.co.knonchalant.candogram.api;

import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ParseMode;
import za.co.knonchalant.candogram.handlers.*;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseBotAPI<T extends IUpdate> implements IBotAPI<T> {
    private final List<IMessageHandler> iMessageHandlers = new ArrayList<>();
    @Override
    public List<IMessageHandler> getHandlers() {
        return iMessageHandlers;
    }

    @Override
    public void setInlineHandler(IInlineHandler handler) {

    }

    @Override
    public IInlineHandler getInlineHandler() {
        return null;
    }

    @Override
    public void addHandler(IMessageHandler handler) {
        iMessageHandlers.add(handler);
    }

    @Override
    public void addHandlers(List<IMessageHandler> handler) {
        for (IMessageHandler iMessageHandler : handler) {
            addHandler(iMessageHandler);
        }
    }


    @Override
    public void sendMessage(T message, String text, Object... args) {

    }

    @Override
    public void sendMessage(T message, String text) {

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
    public void sendPhoto(T chatId, byte[] photoUrl) {

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
