package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.*;

import com.example.demo.dto.UserDTO;
import com.example.demo.model.Order;
import com.example.demo.serviceImpl.OrderService;
import com.example.demo.serviceImpl.UserServiceImpl;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class OrderController {

    private final OrderService orderService;
    private final UserServiceImpl userService;

    public OrderController(OrderService orderService, UserServiceImpl userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping("/users/{id}/orders")
    public List<Order> getUserOrders(@PathVariable int id) {

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

        return orderService.getUserOrders(id);
    }

   @PostMapping("/users/{id}/orders")
public ResponseEntity<?> createOrder(@PathVariable int id,
        @RequestBody Map<String, String> body) {

    try {

        String product = body.get("product");

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
            return ResponseEntity.status(403).body("Access Denied"); // FIX
        }

        //  your original logic
        Order order = orderService.createOrder(id, product);

        return ResponseEntity.ok(order);

    } catch (Exception e) {
        e.printStackTrace(); //  See real error in backend console
        return ResponseEntity.status(500).body("Error: " + e.getMessage());
    }
}
    @DeleteMapping("/users/{id}/orders/{orderId}")
    public ResponseEntity<Map<String, String>> deleteOrder(
            @PathVariable int id,
            @PathVariable int orderId) {

        orderService.deleteOrder(id, orderId);

        return ResponseEntity.ok(Map.of(
                "message", "Order deleted successfully"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/orders")
    public List<Order> getOrders() {
        return orderService.getOrders();
    }
}