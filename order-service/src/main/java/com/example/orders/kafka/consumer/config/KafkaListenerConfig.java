package com.example.orders.kafka.consumer.config;

import com.example.orders.event.CartCheckoutEvent;
import com.example.orders.event.StockRejectedEvent;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;

@Configuration
@RequiredArgsConstructor
public class KafkaListenerConfig {

    private final ConsumerFactory<String, String> baseConsumerFactory;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CartCheckoutEvent>
    checkoutKafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, CartCheckoutEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(
                new DefaultKafkaConsumerFactory<>(
                        baseConsumerFactory.getConfigurationProperties(),
                        new StringDeserializer(),
                        new JacksonJsonDeserializer<>(CartCheckoutEvent.class)
                )
        );

        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, StockRejectedEvent>
    stockRejectedKafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, StockRejectedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(
                new DefaultKafkaConsumerFactory<>(
                        baseConsumerFactory.getConfigurationProperties(),
                        new StringDeserializer(),
                        new JacksonJsonDeserializer<>(StockRejectedEvent.class)
                )
        );

        return factory;
    }
}