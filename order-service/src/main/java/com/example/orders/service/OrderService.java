package com.example.orders.service;

import com.example.orders.dto.OrderDetailsResponse;
import com.example.orders.event.CartCheckoutEvent;
import com.example.orders.dto.OrderDto;
import com.example.orders.event.StockRejectedEvent;

import java.util.List;

public interface OrderService {
    List<OrderDto> getPendingOrders(String userId);
    List<OrderDto> getOrderHistory(String userId);
    OrderDetailsResponse getOrderDetails(String orderId);
    void createOrderFromCheckout(CartCheckoutEvent checkoutEvent);
    void handleStockRejected(StockRejectedEvent stockRejectedEvent);
}
