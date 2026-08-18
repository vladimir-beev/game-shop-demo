package com.example.cart.controller;

import com.example.cart.dto.AddItemRequest;
import com.example.cart.event.CartCheckoutEvent;
import com.example.cart.dto.CartResponse;
import com.example.cart.dto.UpdateQuantityRequest;
import com.example.cart.producer.CartCheckoutProducer;
import com.example.cart.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final CartCheckoutProducer checkoutProducer;

    @GetMapping("/items")
    public ResponseEntity<CartResponse> getCart(@RequestHeader("X-User-Id") String userId) {
        CartResponse response = cartService.getCartProducts(userId);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/items")
    public ResponseEntity<Void> addItem(
            @RequestHeader("X-User-Id") String userId,
            @Valid @RequestBody AddItemRequest addItemRequest
    ) {
        cartService.addItem(userId, addItemRequest);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/items/{itemId}")
    public ResponseEntity<Void> updateItem(
            @RequestHeader("X-User-Id") String userId,
            @PathVariable String itemId,
            @Valid @RequestBody UpdateQuantityRequest updateQuantityRequest
    ) {
        cartService.updateItemQuantity(userId, itemId, updateQuantityRequest.quantity());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> removeItem(
            @RequestHeader("X-User-Id") String userId,
            @PathVariable String itemId
    ) {
        cartService.removeItem(userId, itemId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart(@RequestHeader("X-User-Id") String userId) {
        cartService.clearCart(userId);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/checkout")
    public ResponseEntity<Void> checkout(@RequestHeader("X-User-Id") String userId) {
        CartCheckoutEvent cart = cartService.checkout(userId);
        checkoutProducer.publishCheckout(cart);

        return  ResponseEntity.accepted().build();
    }
}
