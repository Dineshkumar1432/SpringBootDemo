package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

import com.example.demo.event.OrderEvent;
import com.example.demo.kafka.KafkaProducerService;
import com.example.demo.model.Order;
import com.example.demo.model.User;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.UserRepository;



@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Autowired
   private KafkaProducerService kafkaProducerService;

    public OrderService(OrderRepository orderRepository,
            UserRepository userRepository) {

        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

  public Order createOrder(int userId, String product) {

    Order order = new Order();
    order.setProduct(product);
    order.setUser(userRepository.findById(userId).orElseThrow());

    Order savedOrder = orderRepository.save(order);

    // 🔥 SEND EVENT TO KAFKA
    kafkaProducerService.sendOrderEvent(
        new OrderEvent(userId, product)
    );

    return savedOrder;
}
     public List<Order> getUserOrders(int userId) {

        User user = userRepository.findById(userId).orElseThrow();

        return user.getOrders();
    }
    public Order getUserOrder(int userId, int orderId) {
        User user = userRepository.findById(userId).orElseThrow();

        return user.getOrders().stream()
                .filter(o -> o.getId() == orderId)
                .findFirst()
                .orElseThrow();
    }
    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

}