package com.example.auth.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;

@Service
public class JwtServiceImpl implements JwtService {

    private final JwtProperties jwtProps;
    private final Key signingKey;

    public JwtServiceImpl(JwtProperties jwtProps) {
        this.jwtProps = jwtProps;
        this.signingKey = Keys.hmacShaKeyFor(jwtProps.getSecret().getBytes());
    }


    @Override
    public String generateAccessToken(String subject, String userId, String username, Set<String> roles) {

        long validityMilliseconds = jwtProps.getAccessTokenValiditySeconds() * 1000;

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        claims.put("roles", roles);

        return Jwts.builder()
                .setSubject(subject)
                .setIssuer(jwtProps.getIssuer())
                .addClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + validityMilliseconds))
                .signWith(signingKey,  SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String generateRefreshToken(String subject) {

        long validityMilliseconds = jwtProps.getRefreshTokenValiditySeconds() * 1000;

        return Jwts.builder()
                .setSubject(subject)
                .setIssuer(jwtProps.getIssuer())
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + validityMilliseconds))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            String subject = extractSubject(token);
            return subject.equals(userDetails.getUsername()) && !isTokenExpired(token);
        }
        catch (Exception e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    @Override
    public Date extractExpiration(String token) {
        return parseToken(token).getBody().getExpiration();
    }

    @Override
    public String extractSubject(String token) {
        return parseToken(token).getBody().getSubject();
    }

    @Override
    public String extractUserId(String token) {
        return parseToken(token).getBody().get("userId", String.class);
    }

    @Override
    public Set<String> extractRoles(String token) {
        Claims claims = parseToken(token).getBody();

        return Set.of(claims.get("roles").toString());
    }

    @Override
    public Jws<Claims> parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .requireIssuer(jwtProps.getIssuer())
                .build()
                .parseClaimsJws(token);
    }
}
