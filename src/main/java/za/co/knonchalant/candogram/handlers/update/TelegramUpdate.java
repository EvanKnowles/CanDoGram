package za.co.knonchalant.candogram.handlers.update;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Update;
import za.co.knonchalant.candogram.domain.Location;
import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.candogram.handlers.User;

public class TelegramUpdate implements IUpdate {
    private Update update;

    public TelegramUpdate(Update update) {
        this.update = update;
    }

    @Override
    public String getText() {
        if (update.callbackQuery() != null && update.callbackQuery().data() != null) {
            return update.callbackQuery().data();
        }

        return update.message() != null ? update.message().text() : update.inlineQuery().query();
    }

    @Override
    public User getUser() {
        com.pengrad.telegrambot.model.User user = getUserFromUpdate();

        return new User(user.id(), user.username(), user.firstName(), user.lastName());
    }

    private com.pengrad.telegrambot.model.User getUserFromUpdate() {
        if (update.callbackQuery() != null) {
            return update.callbackQuery().from();
        }

        return update.message() != null ? update.message().from() : update.inlineQuery().from();
    }

    @Override
    public User getOtherUser() {
        Chat fromUser = update.message().chat();

        return new User(fromUser.id(), fromUser.username(), fromUser.firstName(), fromUser.lastName());
    }

    @Override
    public long getId() {

        return update.updateId();
    }

    @Override
    public String getInlineId() {
        if (isInline()) {
            return update.inlineQuery().id();
        }
        return null;
    }

    @Override
    public long getMessageId() {
        if (update.callbackQuery() != null) {
            return update.callbackQuery().message().messageId();
        }

        return update.message().messageId();
    }

    @Override
    public long getChatId() {
        if (update.callbackQuery() != null && update.callbackQuery().message() != null) {
            return update.callbackQuery().message().chat().id();
        }
        return update.message() != null ? update.message().chat().id() : -1;
    }

    @Override
    public String getTitle() {
        if (update.callbackQuery() != null && update.callbackQuery().message() != null) {
            return update.callbackQuery().message().chat().title();
        }

        return update.message().chat().title();
    }

    @Override
    public Location getLocation() {
        com.pengrad.telegrambot.model.Location location = update.message().location();
        if (location == null) {
            return null;
        }
        return new Location(location.latitude(), location.longitude());
    }

    @Override
    public boolean isInline() {
        return update.inlineQuery() != null;
    }

    @Override
    public boolean skip() {
        return (update.editedMessage() != null);
    }


}
