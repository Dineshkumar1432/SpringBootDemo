package com.example.demo.dto;

public class OrderDTO {
    private String product;

    public OrderDTO() {

    }

    public OrderDTO(String product) {
        this.product = product;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }
}