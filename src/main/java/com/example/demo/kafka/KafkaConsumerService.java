package com.example.demo.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.demo.event.OrderEvent;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "order-topic", groupId = "order-group")
    public void consume(OrderEvent event) {

        System.out.println("Order Event Received:");
        System.out.println("User ID: " + event.getUserId());
        System.out.println("Product: " + event.getProduct());
    }
}