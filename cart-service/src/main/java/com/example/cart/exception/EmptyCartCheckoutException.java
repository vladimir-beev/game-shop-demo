package com.example.cart.exception;

public class EmptyCartCheckoutException extends RuntimeException {
    public EmptyCartCheckoutException(String userId) {
        super("Cannot checkout empty cart for user: " + userId);
    }
}
