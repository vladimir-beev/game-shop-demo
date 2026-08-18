package com.example.cart.dto;

public record CartItemDto(
        String id,
        String productId,
        int quantity
) {}
