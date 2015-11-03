package com.example.jitter.data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Realm data model for Twitter data
 */
public class TweetRealm extends RealmObject {
    public static final int TYPE_TIMELINE = 0;
    public static final int TYPE_FAVORITES = 1;

    @PrimaryKey
    private long id;

    private String owner;
    private String userName;
    private String message;
    private String imageUrl;
    private boolean isRetweet;
    private int type;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean getIsRetweet() {
        return isRetweet;
    }

    public void setIsRetweet(boolean isRetweet) {
        this.isRetweet = isRetweet;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
