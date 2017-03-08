package com.app.firebasechatapp;

import java.util.Date;

/**
 * Created by Парвиз on 08.03.2017.
 */

public class Message {
    private String author;
    private String message;
    private long date;

    public Message(String author, String message) {
        this.author = author;
        this.message = message;

        this.date = new Date().getTime();
    }

    public Message() {}

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
