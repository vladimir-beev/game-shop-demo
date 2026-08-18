package com.example.cart.event;

import com.example.cart.dto.CartItemDto;

import java.util.List;

public record CartCheckoutEvent(
        String id,
        String userId,
        List<CartItemDto> items
) {}
