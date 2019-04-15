package za.co.knonchalant.candogram;

import com.pengrad.telegrambot.request.SendMessage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatBacklog {
    private Object lastSentLock = new Object();
    private Date lastSent;
    private List<SendMessage> messages = new ArrayList<>();
    private boolean throttle;

    public ChatBacklog(boolean throttle) {
        this.throttle = throttle;
    }

    public long timeLeft() {
        if (lastSent == null || !throttle) {
            return 0;
        }

        long time = lastSent.getTime() + 3000;
        Date date = new Date();

        return time - date.getTime();
    }

    public void add(SendMessage sendMessage) {
        messages.add(sendMessage);
    }

    public boolean isEmpty() {
        return messages.isEmpty();
    }

    public List<SendMessage> getMessages() {
        return messages;
    }

    public void clear() {
        messages.clear();
    }

    public void reset() {
        lastSent = new Date();
    }

    public Object getSentLock() {
        return lastSentLock;
    }
}
