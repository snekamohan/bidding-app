package com.example.auctionapp.model;

public class userModel {
    String id;
    String username;
    String phoneNumber;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public userModel() {
    }

    public userModel(String id, String username, String phoneNumber) {
        this.id = id;
        this.username = username;
        this.phoneNumber = phoneNumber;
    }
}
