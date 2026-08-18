package com.example.inventory.dto;

public record AvailabilityResponse(
        String productId,
        Availability availability
) {}
