package com.example.inventory.service;

import com.example.inventory.dto.AvailabilityResponse;
import com.example.inventory.event.OrderCancelledEvent;
import com.example.inventory.event.OrderCreatedEvent;

public interface InventoryService {

    void handleOrderCreated(OrderCreatedEvent createdEvent);
    void handleOrderCancelled(OrderCancelledEvent cancelledEvent);
    AvailabilityResponse getAvailability(String productId);
}
