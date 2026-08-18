package com.example.auth.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.Set;

public interface JwtService {
    public String generateAccessToken(String subject, String userId, String username, Set<String> roles);

    public String generateRefreshToken(String subject);

    public boolean isTokenValid(String token, UserDetails userDetails);

    public String extractSubject(String token);

    public String extractUserId(String token);

    public Set<String> extractRoles(String token);

    public Date extractExpiration(String token);

    public Jws<Claims> parseToken(String token);
}
