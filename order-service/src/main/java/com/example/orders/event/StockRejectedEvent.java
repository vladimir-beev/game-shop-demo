package com.example.orders.event;

public record StockRejectedEvent(
        String orderId,
        String productId
) {}
