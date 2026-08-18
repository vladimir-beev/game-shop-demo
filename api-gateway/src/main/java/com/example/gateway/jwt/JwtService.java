package com.example.gateway.jwt;

import io.jsonwebtoken.Claims;

import java.util.Set;

public interface JwtService {
    public Claims extractClaims(String token);
    public String extractEmail(String token);
    public String extractUserId(String token);
    public Set<String> extractRoles(String token);
}
