package com.example.demo.dto;

import java.io.Serializable;

public class OrderDTO implements Serializable {
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