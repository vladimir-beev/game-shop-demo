package com.example.orders.event;

import com.example.orders.dto.OrderItemDto;

import java.util.List;

public record OrderCreatedEvent(
        String id,
        List<OrderItemDto> items
) {}
