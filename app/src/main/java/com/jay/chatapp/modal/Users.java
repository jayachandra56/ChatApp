package com.jay.chatapp.modal;

public class Users {
    String username;
    String id;
    String imgURL;

    public Users(String username, String id, String imgURL) {
        this.username = username;
        this.id = id;
        this.imgURL = imgURL;
    }
    public  Users(){

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }
}
