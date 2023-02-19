package za.co.knonchalant.candogram.handlers.update;

import za.co.knonchalant.candogram.domain.Location;
import za.co.knonchalant.candogram.handlers.User;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;

public class TelegramReply extends TelegramUpdate
{
  private final Message replyTo;

  public TelegramReply(Message replyTo)
  {
    super(null);
    this.replyTo = replyTo;
  }


  @Override
  public String getText()
  {
    return replyTo.text();
  }

  @Override
  public User getUser()
  {
    return new User(replyTo.from().id(), replyTo.from().username(), replyTo.from().firstName(), replyTo.from().lastName());
  }

  @Override
  public User getOtherUser()
  {
    Chat fromUser = replyTo.chat();

    return new User(fromUser.id(), fromUser.username(), fromUser.firstName(), fromUser.lastName());
  }

  @Override
  public long getId()
  {
    throw new RuntimeException("Not supported");
  }

  @Override
  public String getInlineId()
  {
    return null;
  }

  @Override
  public long getMessageId()
  {
    return replyTo.messageId();
  }

  @Override
  public long getChatId()
  {
    return replyTo.chat().id();
  }

  @Override
  public String getTitle()
  {
    return replyTo.chat().title();
  }

  @Override
  public Location getLocation()
  {
    return new Location(replyTo.location().latitude(), replyTo.location().longitude());
  }

  @Override
  public TelegramUpdate getReplyTo()
  {
    return new TelegramReply(replyTo.replyToMessage());
  }

  @Override
  public boolean isInline()
  {
    return false;
  }

  @Override
  public boolean skip()
  {
    return false;
  }

  @Override
  public long getSentAt()
  {
    return replyTo.date();
  }
}
