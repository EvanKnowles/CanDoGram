package za.co.knonchalant.candogram.domain;

import com.google.gson.Gson;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by evan on 2016/03/08.
 */
@Entity
public class PendingResponse {
    private int id;

    private long chatId;
    private long userId;
    private String identifier;
    private String keywords;
    private boolean complete;
    private String details;

    public PendingResponse() {
    }

    public PendingResponse(long chatId, long userId, String identifier, BaseDetail details) {
        this.chatId = chatId;
        this.userId = userId;
        this.identifier = identifier;
        setDetails(details);
    }

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public PendingResponse complete() {
        setComplete(true);
        return this;
    }

    @Column(columnDefinition = "LONGTEXT", length = 10_000)
    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setDetails(BaseDetail details) {
        Gson gson = new Gson();
        this.details = gson.toJson(details);
    }
}
