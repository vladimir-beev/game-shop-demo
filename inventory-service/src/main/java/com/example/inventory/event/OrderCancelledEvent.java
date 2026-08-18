package com.example.inventory.event;

import com.example.inventory.dto.OrderItemDto;

import java.util.List;

public record OrderCancelledEvent(
        String id,
        List<OrderItemDto> items
) {}
