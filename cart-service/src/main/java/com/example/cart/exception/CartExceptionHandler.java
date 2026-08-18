package com.example.cart.exception;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class CartExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError ->
                        fieldError.getField() + " " + fieldError.getDefaultMessage()
                )
                .findFirst()
                .orElse("Invalid request");

        Map<String, String> body = Map.of(
                "error", "VALIDATION_ERROR",
                "message", message
        );

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(EmptyCartCheckoutException.class)
    public ResponseEntity<Map<String, String>> handleEmptyCart(EmptyCartCheckoutException ex) {
        Map<String, String> body = Map.of(
                "error", "EMPTY_CART",
                "message", ex.getMessage()
        );

        return ResponseEntity.badRequest().body(body);
    }
}

