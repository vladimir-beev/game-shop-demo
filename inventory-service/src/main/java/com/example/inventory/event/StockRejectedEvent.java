package com.example.inventory.event;

public record StockRejectedEvent(
        String orderId,
        String productId
) {}
