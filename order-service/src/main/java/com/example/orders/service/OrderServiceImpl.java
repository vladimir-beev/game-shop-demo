package com.example.orders.service;

import com.example.orders.client.ProductServiceClient;
import com.example.orders.dto.*;
import com.example.orders.event.CartCheckoutEvent;
import com.example.orders.event.OrderCreatedEvent;
import com.example.orders.event.StockRejectedEvent;
import com.example.orders.entity.Order;
import com.example.orders.entity.OrderItem;
import com.example.orders.entity.OrderStatus;
import com.example.orders.kafka.producer.OrderProducer;
import com.example.orders.repository.OrderRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductServiceClient productServiceClient;
    private final OrderProducer orderProducer;

    @Override
    public List<OrderDto> getPendingOrders(String userId) {
        return orderRepository.findByUserIdAndStatus(userId, OrderStatus.PENDING)
                .stream()
                .map(this::toOrderDto)
                .toList();
    }

    @Override
    public List<OrderDto> getOrderHistory(String userId) {
        return orderRepository.findByUserIdAndStatusIn(
                    userId,
                    List.of(OrderStatus.DELIVERED, OrderStatus.CANCELLED)
                )
                .stream()
                .map(this::toOrderDto)
                .toList();
    }

    @Override
    public OrderDetailsResponse getOrderDetails(String orderId) {

        Order order =  orderRepository.findById(orderId)
                .orElseThrow(NoSuchElementException::new);

        List<ProductSummaryDto> productSummaries = productServiceClient.getProductsByIds(
                order.getItems().stream()
                        .map(OrderItem::getProductId)
                        .toList()
        );

        Map<String, ProductSummaryDto> productMap = productSummaries.stream()
                .collect(Collectors.toMap(ProductSummaryDto::id, productDto -> productDto));

        List<OrderItemResponse> enrichedItems = getEnrichedItems(order, productMap);

        BigDecimal totalPrice = enrichedItems.stream()
                .map(OrderItemResponse::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new OrderDetailsResponse(
                order.getId(),
                enrichedItems,
                totalPrice
        );
    }

    @Override
    public void createOrderFromCheckout(CartCheckoutEvent checkoutEvent) {
        Order order = new Order();
        order.setUserId(checkoutEvent.userId());

        checkoutEvent.items().forEach(itemDto -> {
            OrderItem item = new OrderItem();
            item.setProductId(itemDto.productId());
            item.setQuantity(itemDto.quantity());
            order.addItem(item);
        });

        orderRepository.save(order);

        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent(
                order.getId(),
                order.getItems().stream()
                        .map(this::toOrderItemDto)
                        .toList()
        );

        orderProducer.publishOrderCreated(orderCreatedEvent, order.getUserId());
    }

    @Override
    public void handleStockRejected(StockRejectedEvent stockRejectedEvent) {
        Order order = orderRepository.findById(stockRejectedEvent.orderId())
                .orElseThrow(() -> new NoSuchElementException(
                        "Order not found for orderId: " + stockRejectedEvent.orderId()
                ));

        if (order.getStatus() == OrderStatus.CANCELLED) {
            return;
        }

        order.setStatus(OrderStatus.CANCELLED);
        order.setCancellationReason("Insufficient stock");
        order.setRejectedProductId(stockRejectedEvent.productId());
        order.setCanceledAt(Instant.now());

        orderRepository.save(order);
    }

    private @NonNull OrderDto toOrderDto(Order order) {
        return new OrderDto(
                order.getId(),
                order.getUserId(),
                order.getStatus().name(),
                order.getCancellationReason(),
                order.getRejectedProductId(),
                order.getCanceledAt(),
                order.getCreatedAt(),
                order.getItems().stream()
                        .map(this::toOrderItemDto)
                        .toList()
        );
    }

    private @NonNull OrderItemDto toOrderItemDto(OrderItem orderItem) {
        return new OrderItemDto(
                orderItem.getId(),
                orderItem.getProductId(),
                orderItem.getQuantity()
        );
    }

    private static @NonNull List<OrderItemResponse> getEnrichedItems(
            Order order,
            Map<String, ProductSummaryDto> productMap
    ) {
        return order.getItems().stream()
                .map(item -> {
                    ProductSummaryDto product = productMap.get(item.getProductId());
                    BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());
                    BigDecimal subtotal = product.price().multiply(quantity);

                    if (product.productType().equals("GAME")) {
                        return new OrderItemResponse(
                                item.getId(),
                                product.title(),
                                product.productType(),
                                product.platform(),
                                item.getQuantity(),
                                subtotal
                        );
                    }

                    return new OrderItemResponse(
                            item.getId(),
                            product.title(),
                            product.productType(),
                            null,
                            item.getQuantity(),
                            subtotal
                    );
                })
                .toList();
    }
}
