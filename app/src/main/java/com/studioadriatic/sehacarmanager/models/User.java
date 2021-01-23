package com.studioadriatic.sehacarmanager.models;

/**
 * Created by Andrej on 13.2.2016..
 */
public class User {
    public static final int ADMIN = 0;
    public static final int MODERATOR = 1;
    public static final int DRIVER = 2;
    public static final int REFEREE_DRIVER = 3;

    private int id;
    private String email;
    private int type;
    private String name;
    private String token;
    private String gcm_id;
    private String picture;

    public void setId(int id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setGcm_id(String gcm_id) {
        this.gcm_id = gcm_id;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getToken() {
        return token;
    }

    public String getGcm_id() {
        return gcm_id;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
