package com.example.orders.event;

import com.example.orders.dto.CartItemDto;

import java.util.List;

public record CartCheckoutEvent(
        String id,
        String userId,
        List<CartItemDto> items
) {}
