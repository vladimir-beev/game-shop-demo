package com.example.orders.kafka.producer;

import com.example.orders.dto.OrderDto;
import com.example.orders.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderProducer {

    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    @Value("${topic.order-created}")
    private String orderCreatedTopic;

    public void publishOrderCreated(OrderCreatedEvent orderCreatedEvent, String userId) {
        kafkaTemplate.send(orderCreatedTopic, userId, orderCreatedEvent);
    }
}

