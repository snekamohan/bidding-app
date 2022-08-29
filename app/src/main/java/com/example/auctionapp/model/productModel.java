package com.example.auctionapp.model;

public class productModel {
    private String id;
    private String username;
    private String phoneNumber;
    private String foodImage;
    private String foodName;
    private String foodDesc;
    private String foodPrice;
    private String bidder;
    private String bid;

    public String getBidder() {
        return bidder;
    }

    public void setBidder(String bidder) {
        this.bidder = bidder;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public productModel(String id, String username, String phoneNumber, String foodImage, String foodName, String foodDesc, String foodPrice, String bidder, String bid) {
        this.id = id;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.foodImage = foodImage;
        this.foodName = foodName;
        this.foodDesc = foodDesc;
        this.foodPrice = foodPrice;
        this.bidder = bidder;
        this.bid = bid;
    }

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

    public String getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodDesc() {
        return foodDesc;
    }

    public void setFoodDesc(String foodDesc) {
        this.foodDesc = foodDesc;
    }

    public String getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(String foodPrice) {
        this.foodPrice = foodPrice;
    }

    public productModel() {
    }

    public productModel(String id, String username, String phoneNumber, String foodImage, String foodName, String foodDesc, String foodPrice) {
        this.id = id;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.foodImage = foodImage;
        this.foodName = foodName;
        this.foodDesc = foodDesc;
        this.foodPrice = foodPrice;
    }
}
