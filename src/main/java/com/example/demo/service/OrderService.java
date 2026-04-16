package com.example.demo.service;

import org.springframework.stereotype.Service;
import java.util.*;

import com.example.demo.model.Order;
import com.example.demo.model.User;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.UserRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository,
            UserRepository userRepository) {

        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    public Order createOrder(int userId, String product) {

        User user = userRepository.findById(userId).orElseThrow();

        Order order = new Order(product, user);

        return orderRepository.save(order);
    }
     public List<Order> getUserOrders(int userId) {

        User user = userRepository.findById(userId).orElseThrow();

        return user.getOrders();
    }
    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

}