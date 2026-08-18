package com.example.auth.service;

import com.example.auth.dto.TokenPair;
import com.example.auth.dto.LoginRequest;
import com.example.auth.dto.RegisterRequest;
import com.example.auth.entity.RefreshToken;
import com.example.auth.entity.RoleName;
import com.example.auth.entity.User;
import com.example.auth.entity.UserRole;
import com.example.auth.exception.InvalidCredentialsException;
import com.example.auth.exception.InvalidUserRoleException;
import com.example.auth.exception.UserAlreadyExistsException;
import com.example.auth.repository.RefreshTokenRepository;
import com.example.auth.repository.RoleRepository;
import com.example.auth.repository.UserRepository;
import com.example.auth.security.jwt.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public void register(RegisterRequest registerRequest) {

        if (userRepository.existsByEmail(registerRequest.email())) {
            throw new UserAlreadyExistsException("Email already exists.");
        }

        if (userRepository.existsByUsername(registerRequest.username())) {
            throw new UserAlreadyExistsException("Username already exists.");
        }

        UserRole userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new InvalidUserRoleException("Role Not Found"));

        User user = new User();
        user.setEmail(registerRequest.email());
        user.setUsername(registerRequest.username());
        user.setPasswordHash(passwordEncoder.encode(registerRequest.password()));
        user.setRoles(Set.of(userRole));

        userRepository.save(user);
    }

    @Override
    public TokenPair login(LoginRequest loginRequest) {

        User user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid Credentials"));

        if (!passwordEncoder.matches(loginRequest.password(), user.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid Credentials");
        }

        //Check for existing token for that user and clear it
        refreshTokenRepository.findByUserId(user.getId())
                        .ifPresent(refreshTokenRepository::delete);

        TokenPair tokens = generateTokens(user);

        saveRefreshToken(tokens, user);

        return tokens;
    }

    @Override
    @Transactional
    public TokenPair renewTokens(String refreshToken) {

        checkStoredTokenValidity(refreshToken);

        Claims claimsBody;

        try {
            claimsBody = jwtService.parseToken(refreshToken).getBody();
        }
        catch (Exception e) {
            deleteOldRefreshToken(refreshToken);
            throw new InvalidCredentialsException("Invalid refresh token");
        }

        if (claimsBody.get("roles") != null) {
            throw new InvalidCredentialsException("Access token cannot be used to refresh");
        }

        User user = userRepository.findByEmail(claimsBody.getSubject())
                .orElseThrow(() -> new InvalidCredentialsException("User not found"));

        deleteOldRefreshToken(refreshToken);

        TokenPair tokens = generateTokens(user);

        saveRefreshToken(tokens, user);

        return tokens;
    }

    @Override
    public void logout(String refreshToken) {
        refreshTokenRepository.findByToken(refreshToken)
                .ifPresent(refreshTokenRepository::delete);
    }

    private void checkStoredTokenValidity(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new InvalidCredentialsException("Missing refresh token");
        }

        RefreshToken storedToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid refresh token"));

        if (storedToken.getExpiresAt().isBefore(Instant.now())) {
            deleteOldRefreshToken(refreshToken);
            throw new InvalidCredentialsException("Refresh token expired");
        }
    }

    private void deleteOldRefreshToken(String refreshToken) {
        refreshTokenRepository.findByToken(refreshToken)
                .ifPresent(refreshTokenRepository::delete);
    }

    private TokenPair generateTokens(User user) {

        Set<String> roles = extractRoles(user);

        String accessToken = jwtService.generateAccessToken(
                user.getEmail(),
                user.getId(),
                user.getUsername(),
                roles
        );

        String refreshToken = jwtService.generateRefreshToken(user.getEmail());

        return new TokenPair(accessToken, refreshToken);
    }

    private void saveRefreshToken(TokenPair tokens, User user) {
        RefreshToken rt = new RefreshToken();
        rt.setToken(tokens.refreshToken());
        rt.setUser(user);
        rt.setExpiresAt(jwtService.extractExpiration(tokens.refreshToken()).toInstant());

        refreshTokenRepository.save(rt);
    }

    private static Set<String> extractRoles(User user) {
        return user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());
    }
}
