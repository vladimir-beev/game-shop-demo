package com.example.orders.controller;

import com.example.orders.dto.OrderDetailsResponse;
import com.example.orders.dto.OrderDto;
import com.example.orders.entity.OrderStatus;
import com.example.orders.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/pending")
    public ResponseEntity<List<OrderDto>> getPendingOrders(@RequestHeader("X-User-Id") String userId) {
        List<OrderDto> orders = orderService.getPendingOrders(userId);

        return ResponseEntity.ok(orders);
    }

    @GetMapping("/history")
    public ResponseEntity<List<OrderDto>> getOrderHistory(@RequestHeader("X-User-Id") String userId) {
        List<OrderDto> orders = orderService.getOrderHistory(userId);

        return ResponseEntity.ok(orders);
    }

    @GetMapping("/details/{orderId}")
    public ResponseEntity<OrderDetailsResponse> getOrderDetails(@PathVariable String orderId) {
        OrderDetailsResponse response = orderService.getOrderDetails(orderId);

        return ResponseEntity.ok(response);
    }
}
