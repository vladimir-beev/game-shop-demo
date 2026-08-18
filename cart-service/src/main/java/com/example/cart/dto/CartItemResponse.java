package com.example.cart.dto;

import java.math.BigDecimal;

public record CartItemResponse(
        String id,
        String productId,
        String title,
        BigDecimal price,
        String productType,
        String platform, // game-specific
        int quantity,
        BigDecimal subtotal
) {}
