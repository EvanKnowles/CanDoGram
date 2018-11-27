package za.co.knonchalant.candogram.handlers;

import za.co.knonchalant.candogram.domain.Location;

public interface IUpdate {
    public String getText();
    public User getUser();
    public User getOtherUser();
    public long getId();
    public String getInlineId();
    public long getMessageId();
    public long getChatId();
    public String getTitle();
    public Location getLocation();

    public boolean isInline();

    boolean skip();
}
