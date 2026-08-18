package com.example.orders.dto;

import java.time.Instant;
import java.util.List;

public record OrderDto(
        String id,
        String userId,
        String status,
        String cancellationReason,
        String rejectedProductId,
        Instant canceledAt,
        Instant createdAt,
        List<OrderItemDto> items
) {}
