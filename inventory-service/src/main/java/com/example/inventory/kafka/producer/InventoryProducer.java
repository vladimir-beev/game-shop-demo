package com.example.inventory.kafka.producer;

import com.example.inventory.event.StockRejectedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryProducer {

    @Value("${topic.stock-rejected}")
    private String stockRejectedTopic;

    private final KafkaTemplate<String, StockRejectedEvent> kafkaTemplate;

    public void publishStockRejected(String orderId, String productId) {
        kafkaTemplate.send(
                stockRejectedTopic,
                orderId,
                new StockRejectedEvent(orderId, productId)
        );
    }
}

