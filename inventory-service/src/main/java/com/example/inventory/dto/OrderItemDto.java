package com.example.inventory.dto;

public record OrderItemDto(
        String id,
        String productId,
        int quantity
) {}
