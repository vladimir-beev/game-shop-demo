package com.example.auth.service;

import com.example.auth.dto.TokenPair;
import com.example.auth.dto.LoginRequest;
import com.example.auth.dto.RegisterRequest;

public interface AuthService {

    void register(RegisterRequest registerRequest);
    TokenPair login(LoginRequest loginRequest);
    TokenPair renewTokens(String token);
    void logout(String email);
}
