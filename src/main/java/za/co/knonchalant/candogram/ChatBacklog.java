package za.co.knonchalant.candogram;

import com.pengrad.telegrambot.request.SendMessage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatBacklog {
    private Object lastSentLock = new Object();
    private Date lastSent;
    private List<SendMessage> messages = new ArrayList<>();

    public long timeLeft() {
        if (lastSent == null) {
            return 0;
        }
        long time = lastSent.getTime() + 1000;
        Date date = new Date();

        return date.getTime() - time;
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
