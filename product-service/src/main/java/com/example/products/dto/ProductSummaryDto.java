package com.example.products.dto;

import java.math.BigDecimal;

public record ProductSummaryDto(
        String id,
        String title,
        BigDecimal price,
        String productType,
        String platform // game-specific
) {}
