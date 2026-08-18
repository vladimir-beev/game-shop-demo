package com.example.products.dto;

import java.math.BigDecimal;

public record ProductDto(
        String id,
        String title,
        BigDecimal price,
        String coverImageUrl,
        String description,
        String sku,
        String productType
) {}

