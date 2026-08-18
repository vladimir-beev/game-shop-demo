package com.example.inventory.service;

import com.example.inventory.dto.Availability;
import com.example.inventory.dto.AvailabilityResponse;
import com.example.inventory.event.OrderCancelledEvent;
import com.example.inventory.event.OrderCreatedEvent;
import com.example.inventory.dto.OrderItemDto;
import com.example.inventory.entity.ProductStock;
import com.example.inventory.kafka.producer.InventoryProducer;
import com.example.inventory.repository.ProductStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final ProductStockRepository productStockRepository;
    private final InventoryProducer producer;

    @Override
    public void handleOrderCreated(OrderCreatedEvent order) {
        for (OrderItemDto item : order.items()) {
            ProductStock stock = productStockRepository.findById(item.productId())
                    .orElseThrow(() -> new NoSuchElementException(
                            "Product stock not found for productId: " + item.productId()
                    ));

            // When insufficient stock
            if (stock.getAvailableQuantity() < item.quantity()) {
                producer.publishStockRejected(order.id(), item.productId());
                return;
            }

            stock.setAvailableQuantity(stock.getAvailableQuantity() - item.quantity());
            productStockRepository.save(stock);
        }
    }

    @Override
    public void handleOrderCancelled(OrderCancelledEvent cancelledEvent) {
        for (OrderItemDto item : cancelledEvent.items()) {
            ProductStock stock = productStockRepository.findById(item.productId())
                    .orElseThrow(() -> new NoSuchElementException(
                            "Product stock not found for productId: " + item.productId()
                    ));

            stock.setAvailableQuantity(stock.getAvailableQuantity() + item.quantity());
            productStockRepository.save(stock);
        }
    }

    @Override
    public AvailabilityResponse getAvailability(String productId) {
        ProductStock stock = productStockRepository.findById(productId)
                .orElseThrow(() -> new IllegalStateException(
                        "No stock entry found for productId: " + productId
                ));

        Availability availability = mapToAvailability(stock.getAvailableQuantity());

        return new AvailabilityResponse(productId, availability);
    }

    private Availability mapToAvailability(int quantity) {
        if (quantity == 0) {
            return Availability.OUT_OF_STOCK;
        }
        else if (quantity <= 5) {
            return Availability.LOW_STOCK;
        }

        return Availability.IN_STOCK;
    }
}
