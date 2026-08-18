package com.example.gateway.filter;

import jakarta.annotation.Nonnull;
import lombok.Data;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RoleCheckGatewayFilterFactory extends AbstractGatewayFilterFactory<RoleCheckGatewayFilterFactory.Config> {

    public RoleCheckGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            String rolesHeader = exchange.getRequest()
                    .getHeaders()
                    .getFirst("X-User-Roles");

            if (rolesHeader == null || rolesHeader.isBlank()) {
                return forbidden(exchange, "Missing roles");
            }

            Set<String> userRoles = extractUserRolesFromHeader(rolesHeader);

            Set<String> requiredRoles = getRequiredRoles(config);

            if (requiredRoles.isEmpty()) {
                return forbidden(exchange, "No required roles configured");
            }

            boolean hasRole = userRoles.stream()
                    .anyMatch(requiredRoles::contains);

            if (!hasRole) {
                return forbidden(exchange, "Insufficient role");
            }

            return chain.filter(exchange);
        };
    }

    @Data
    public static class Config {
        private String requiredRoles;
    }

    @Nonnull
    private static Set<String> extractUserRolesFromHeader(String rolesHeader) {
        return Arrays.stream(rolesHeader.split(","))
                .map(String::trim)
                .filter(r -> !r.isBlank())
                .collect(Collectors.toSet());
    }

    @Nonnull
    private static Set<String> getRequiredRoles(Config config) {
        return Arrays.stream(config.getRequiredRoles().split(","))
                .map(String::trim)
                .filter(r -> !r.isBlank())
                .collect(Collectors.toSet());
    }

    private Mono<Void> forbidden(ServerWebExchange exchange, String message) {
        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String body = String.format(
                "{\"status\":403,\"error\":\"Forbidden\",\"message\":\"%s\",\"timestamp\":\"%s\"}",
                message,
                Instant.now().toString()
        );

        DataBuffer buffer = exchange.getResponse()
                .bufferFactory()
                .wrap(body.getBytes(StandardCharsets.UTF_8));

        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
}

