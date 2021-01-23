package com.studioadriatic.sehacarmanager.models;

/**
 * Created by kristijandraca@gmail.com
 */
public class Message {
    private int id;
    private int chat_id;

    private User sender;
    private String message;
    private String time;

    public void setId(int id) {
        this.id = id;
    }

    public void setChat_id(int chat_id) {
        this.chat_id = chat_id;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public int getChat_id() {
        return chat_id;
    }

    public User getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public String getTime() {
        return time;
    }

}