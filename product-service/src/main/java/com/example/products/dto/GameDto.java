package com.example.products.dto;

import com.example.products.entity.games.Genre;
import com.example.products.entity.games.Platform;

import java.math.BigDecimal;
import java.time.LocalDate;

public record GameDto(
        String id,
        String title,
        BigDecimal price,
        String coverImageUrl,
        String description,
        String sku,
        String productType,
        Platform platform,
        Genre genre,
        LocalDate releaseDate,
        String publisher,
        String developer
) {}
