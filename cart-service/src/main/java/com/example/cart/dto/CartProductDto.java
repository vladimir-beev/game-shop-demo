package com.example.cart.dto;

import java.math.BigDecimal;

public record CartProductDto(
        String id,
        String title,
        BigDecimal price,
        String productType,
        String platform // game-specific
) {}
