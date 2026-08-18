package com.example.cart.producer.config;

import com.example.cart.event.CartCheckoutEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaProducerConfig {

    @Bean
    public KafkaTemplate<String, CartCheckoutEvent> kafkaTemplate(
            ProducerFactory<String, CartCheckoutEvent> producerFactory
    ) {
        return new KafkaTemplate<>(producerFactory);
    }
}
