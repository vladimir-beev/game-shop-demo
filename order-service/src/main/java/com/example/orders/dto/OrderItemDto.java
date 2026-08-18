package com.example.orders.dto;

public record OrderItemDto(
        String id,
        String productId,
        int quantity
) {}
