package com.example.cart.producer;

import com.example.cart.event.CartCheckoutEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartCheckoutProducer {

    @Value("${topic.checkout}")
    private String checkoutTopic;

    private final KafkaTemplate<String, CartCheckoutEvent> kafkaTemplate;

    public void publishCheckout(CartCheckoutEvent checkoutEvent) {
        kafkaTemplate.send(checkoutTopic, checkoutEvent.userId(), checkoutEvent);
    }
}

