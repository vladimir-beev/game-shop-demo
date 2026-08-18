package com.example.inventory.kafka.producer.config;

import com.example.inventory.event.StockRejectedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaProducerConfig {

    @Bean
    public KafkaTemplate<String, StockRejectedEvent> kafkaTemplate(
            ProducerFactory<String, StockRejectedEvent> producerFactory
    ) {
        return new KafkaTemplate<>(producerFactory);
    }
}
