
package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = false)

public class UserDTO implements Serializable {
    @NotBlank(message = "Name cannot be empty")
    @Size(min = 3, message = "Name must have at least 3 characters")
    @NotEmpty(message = "Name cannot be empty")
    private int id;
    private String name;
    private String username;
    private String role;
    private List<OrderDTO> orders;

    public UserDTO() {
    }

    public UserDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<OrderDTO> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderDTO> orders) {
        this.orders = orders;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }   
}