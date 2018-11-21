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
        return update.message() != null ? update.message().text() : update.inlineQuery().query();
    }

    @Override
    public User getUser() {
        com.pengrad.telegrambot.model.User user = update.message() != null ? update.message().from() : update.inlineQuery().from();

        return new User(user.id(), user.username(), user.firstName(), user.lastName());
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
        if (isInline()){
            return update.inlineQuery().id();
        }
        return null;
    }

    @Override
    public long getMessageId() {
        return update.message().messageId();
    }

    @Override
    public long getChatId() {
        return update.message() != null ? update.message().chat().id() : -1;
    }

    @Override
    public String getTitle() {
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


}
