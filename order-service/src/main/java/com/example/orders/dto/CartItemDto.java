package com.example.orders.dto;

public record CartItemDto(
        String id,
        String productId,
        int quantity
) {}
