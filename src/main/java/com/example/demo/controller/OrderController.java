package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;

import com.example.demo.model.Order;
import com.example.demo.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    @GetMapping("/users/{id}/orders")
    public List<Order> getUserOrders(@PathVariable int id) {
        return orderService.getUserOrders(id);
    }
    public String getMethodName(@RequestParam String param) {
        return new String();
    }
    
    @PostMapping("/users/{id}/orders")
    public Order createOrder(@PathVariable int id,
            @RequestParam String product) {

        return orderService.createOrder(id, product);
    }

    @GetMapping("/orders")
    public List<Order> getOrders() {
        return orderService.getOrders();
    }

}
