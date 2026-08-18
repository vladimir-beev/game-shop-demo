package com.example.gateway;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication(exclude = ReactiveUserDetailsServiceAutoConfiguration.class)
public class SpringApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringApiGatewayApplication.class, args);

        System.out.println("Spring API Gateway Application has started successfully");
    }

   /* @Bean
    public ApplicationRunner runner(List<GatewayFilterFactory<?>> factories) {
        return args -> factories.forEach(f ->
                System.out.println("Factory: " + f.getClass().getName())
        );
    }*/
}
