package com.example.orders.kafka.consumer;

import com.example.orders.event.CartCheckoutEvent;
import com.example.orders.event.StockRejectedEvent;
import com.example.orders.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceListener {

    private final OrderService orderService;

    @KafkaListener(
            topics = "${topic.checkout}",
            groupId = "order-service",
            containerFactory = "checkoutKafkaListenerContainerFactory"
    )
    public void handleCheckout(CartCheckoutEvent checkoutEvent) {
        orderService.createOrderFromCheckout(checkoutEvent);
    }

    @KafkaListener(
            topics = "${topic.stock-rejected}",
            groupId = "order-service",
            containerFactory = "stockRejectedKafkaListenerContainerFactory"
    )
    public void handleStockRejected(StockRejectedEvent stockRejectedEvent) {
        orderService.handleStockRejected(stockRejectedEvent);
    }
}
