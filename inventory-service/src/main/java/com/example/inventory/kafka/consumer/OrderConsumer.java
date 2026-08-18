package com.example.inventory.kafka.consumer;

import com.example.inventory.event.OrderCancelledEvent;
import com.example.inventory.event.OrderCreatedEvent;
import com.example.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderConsumer {

    private final InventoryService inventoryService;

    @KafkaListener(topics = "${topic.order-created}", groupId = "inventory-service")
    public void handleOrderCreated(OrderCreatedEvent orderCreatedEvent) {
        inventoryService.handleOrderCreated(orderCreatedEvent);
    }

    @KafkaListener(topics = "${topic.order-cancelled}", groupId = "inventory-service")
    public void handleOrderCancelled(OrderCancelledEvent cancelledEvent) {
        inventoryService.handleOrderCancelled(cancelledEvent);
    }
}
