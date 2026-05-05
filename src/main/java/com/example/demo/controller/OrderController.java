package com.example.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.*;

import com.example.demo.dto.UserDTO;
import com.example.demo.model.Order;
import com.example.demo.service.OrderService;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/api")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ADMIN') or @userService.getUser(#id).username == authentication.name")
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

        String loggedInUser = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        boolean isAdmin = SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        UserDTO user = userService.getUser(id);

        if (!user.getUsername().equals(loggedInUser) && !isAdmin) {
            throw new RuntimeException("Access Denied");
        }

        return orderService.createOrder(id, product);
    }

    @GetMapping("/users/{id}/orders/{orderId}")
    public Order getUserOrder(@PathVariable int id,
            @PathVariable int orderId) {

        String loggedInUser = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        boolean isAdmin = SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        UserDTO user = userService.getUser(id);

        // Ownership check
        if (!user.getUsername().equals(loggedInUser) && !isAdmin) {
            throw new RuntimeException("Access Denied");
        }

        return orderService.getUserOrder(id, orderId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/orders")
    public List<Order> getOrders() {
        return orderService.getOrders();
    }

}
