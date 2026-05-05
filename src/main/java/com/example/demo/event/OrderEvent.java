package com.example.demo.event;

public class OrderEvent {

    private int userId;
    private String product;

    public OrderEvent() {}

    public OrderEvent(int userId, String product) {
        this.userId = userId;
        this.product = product;
    }

    public int getUserId() { return userId; }
    public String getProduct() { return product; }

    public void setUserId(int userId) { this.userId = userId; }
    public void setProduct(String product) { this.product = product; }
}