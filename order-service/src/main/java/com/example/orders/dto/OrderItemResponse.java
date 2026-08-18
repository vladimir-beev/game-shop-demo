package com.example.orders.dto;

import java.math.BigDecimal;

public record OrderItemResponse(
        String id,
        String title,
        String productType,
        String platform, // game-specific
        int quantity,
        BigDecimal subtotal
) {}
