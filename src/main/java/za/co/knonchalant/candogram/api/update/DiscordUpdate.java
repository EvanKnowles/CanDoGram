package za.co.knonchalant.candogram.api.update;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import za.co.knonchalant.candogram.domain.Location;
import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.candogram.handlers.User;

public class DiscordUpdate implements IUpdate {
    private Message msg;
    private User user;

    public DiscordUpdate(Message msg) {
        this.msg = msg;

        net.dv8tion.jda.api.entities.User author = msg.getAuthor();

        user = new User(author.getIdLong(), author.getName(), author.getName(), "");
    }

    @Override
    public String getText() {
        return msg.getContentRaw();
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public User getOtherUser() {
        return null;
    }

    @Override
    public long getId() {
        return msg.getIdLong();
    }

    @Override
    public String getInlineId() {
        return null;
    }

    @Override
    public long getMessageId() {
        return msg.getIdLong();
    }

    @Override
    public long getChatId() {
        return msg.getChannel().getLatestMessageIdLong();
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public Location getLocation() {
        return null;
    }

    @Override
    public IUpdate getReplyTo()
    {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public boolean isInline() {
        return false;
    }

    @Override
    public boolean skip() {
        return false;
    }

    public MessageChannel getChannel() {
        return msg.getChannel();
    }

    @Override
    public long getSentAt()
    {
        throw new RuntimeException("Not implemented");
    }
}
