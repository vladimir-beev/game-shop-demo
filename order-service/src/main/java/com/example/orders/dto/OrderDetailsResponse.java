package com.example.orders.dto;

import java.math.BigDecimal;
import java.util.List;

public record OrderDetailsResponse(
        String orderId,
        List<OrderItemResponse> items,
        BigDecimal totalPrice
) {}
