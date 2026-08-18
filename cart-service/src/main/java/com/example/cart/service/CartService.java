package com.example.cart.service;

import com.example.cart.dto.AddItemRequest;
import com.example.cart.event.CartCheckoutEvent;
import com.example.cart.dto.CartResponse;

public interface CartService {

    void addItem(String userId, AddItemRequest addItemRequest);

    void updateItemQuantity(String userId, String itemId, int quantity);

    void removeItem(String userId, String itemId);

    CartResponse getCartProducts(String userId);

    void clearCart(String userId);

    CartCheckoutEvent checkout(String userId);
}
