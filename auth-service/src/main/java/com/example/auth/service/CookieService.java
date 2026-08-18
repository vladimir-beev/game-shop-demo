package com.example.auth.service;

import org.springframework.http.ResponseCookie;

public interface CookieService {
    public ResponseCookie createRefreshCookie(String refreshToken);
    public ResponseCookie deleteRefreshCookie();
}
