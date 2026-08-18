package com.example.gateway.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JwtServiceImpl implements JwtService {

    private final Key signingKey;
    private final String expectedIssuer;

    public JwtServiceImpl(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.issuer}") String issuer
    ) {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.expectedIssuer = issuer;
    }

    public Claims extractClaims(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .requireIssuer(expectedIssuer)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            validateExpiration(claims);

            return claims;
        }
        catch (JwtException e) {
            throw new RuntimeException("Invalid or expired token", e);
        }
    }

    private void validateExpiration(Claims claims) {
        Date expiration = claims.getExpiration();

        if (expiration == null || expiration.before(new Date())) {
            throw new RuntimeException("JWT token is expired");
        }
    }

    @Override
    public String extractUserId(String token) {
        return extractClaims(token).get("userId").toString();
    }

    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    public Set<String> extractRoles(String token) {
        Claims claims = extractClaims(token);

        Object roles = claims.get("roles");

        if (roles instanceof List<?> list) {
            return list.stream()
                    .map(Object::toString)
                    .collect(Collectors.toSet());
        }

        return Set.of();
    }
}

