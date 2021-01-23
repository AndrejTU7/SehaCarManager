package com.studioadriatic.sehacarmanager.models;

/**
 * Created by kristijandraca@gmail.com
 */
public class Chats {

    private int id;
    private User user_1;
    private User user_2;

    private Message last_message;

    public void setId(int id) {
        this.id = id;
    }

    public void setUser_1(User user_1) {
        this.user_1 = user_1;
    }

    public void setUser_2(User user_2) {
        this.user_2 = user_2;
    }

    public void setLast_message(Message last_message) {
        this.last_message = last_message;
    }

    public int getId() {
        return id;
    }

    public User getUser_1() {
        return user_1;
    }

    public User getUser_2() {
        return user_2;
    }

    public Message getLast_message() {
        return last_message;
    }

}
