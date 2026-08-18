package com.example.gateway.config;

import com.example.gateway.filter.JwtAuthGatewayFilterFactory;
import com.example.gateway.filter.RoleCheckGatewayFilterFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GatewayConfig {

    private final JwtAuthGatewayFilterFactory jwtAuthFilter;
    private final RoleCheckGatewayFilterFactory roleCheckFilter;

    @Bean
    public JwtAuthGatewayFilterFactory jwtAuthFilterFactory() {
        return jwtAuthFilter;
    }

    @Bean
    public RoleCheckGatewayFilterFactory roleCheckFilterFactory() {
        return roleCheckFilter;
    }
}

