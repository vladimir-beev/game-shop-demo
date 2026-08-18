package com.example.cart.service;

import com.example.cart.client.ProductServiceClient;
import com.example.cart.dto.*;
import com.example.cart.entity.Cart;
import com.example.cart.entity.CartItem;
import com.example.cart.entity.CartStatus;
import com.example.cart.event.CartCheckoutEvent;
import com.example.cart.exception.EmptyCartCheckoutException;
import com.example.cart.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductServiceClient productServiceClient;

    @Override
    public void addItem(String userId, AddItemRequest addItemRequest) {
        Cart cart = getOrCreateCart(userId);

        CartItem existingItem = cart.getItems().stream()
                .filter(cartItem ->
                        cartItem.getProductId().equals(addItemRequest.productId())
                )
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + addItemRequest.quantity());
        }
        else {
            CartItem item = new CartItem();
            item.setProductId(addItemRequest.productId());
            item.setQuantity(addItemRequest.quantity());
            cart.addItem(item);
        }

        cartRepository.save(cart);
    }

    @Override
    public void updateItemQuantity(String userId, String itemId, int quantity) {
        Cart cart = getOrCreateCart(userId);

        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow();

        cartItem.setQuantity(quantity);

        cartRepository.save(cart);
    }


    @Override
    public void removeItem(String userId, String itemId) {
        Cart cart = getOrCreateCart(userId);
        cart.getItems().removeIf(cartItem -> cartItem.getId().equals(itemId));

        cartRepository.save(cart);
    }

    @Override
    public CartResponse getCartProducts(String userId) {

        Cart cart = getOrCreateCart(userId);

        List<String> productIds = cart.getItems().stream()
                .map(CartItem::getProductId)
                .toList();

        List<CartProductDto> products = productServiceClient.getProductsByIds(productIds);

        Map<String, CartProductDto> productMap = products.stream()
                .collect(Collectors.toMap(CartProductDto::id, productDto -> productDto));

        List<CartItemResponse> enrichedItems = getEnrichedItems(cart, productMap);

        BigDecimal totalPrice = enrichedItems.stream()
                .map(CartItemResponse::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartResponse(enrichedItems, totalPrice);
    }


    @Override
    public void clearCart(String userId) {
        Cart cart = getOrCreateCart(userId);
        cart.getItems().clear();

        cartRepository.save(cart);
    }

    @Override
    public CartCheckoutEvent checkout(String userId) {
        Cart cart = getOrCreateCart(userId);

        if (cart.getItems().isEmpty()) {
            throw new EmptyCartCheckoutException(userId);
        }

        cart.setStatus(CartStatus.COMPLETED);
        cartRepository.save(cart);

        return toCheckoutEvent(cart);
    }

    private @NonNull Cart getOrCreateCart(String userId) {
        return cartRepository.findByUserIdAndStatus(userId, CartStatus.ACTIVE)
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUserId(userId);
                    return cartRepository.save(cart);
                });
    }

    private static @NonNull List<CartItemResponse> getEnrichedItems(
            Cart cart,
            Map<String, CartProductDto> productMap
    ) {
        return cart.getItems().stream()
                .map(item -> {
                    CartProductDto product = productMap.get(item.getProductId());
                    BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());
                    BigDecimal subtotal = product.price().multiply(quantity);

                    if (product.productType().equals("GAME")) {
                        return new CartItemResponse(
                                item.getId(),
                                product.id(),
                                product.title(),
                                product.price(),
                                product.productType(),
                                product.platform(),
                                item.getQuantity(),
                                subtotal
                        );
                    }

                    return new CartItemResponse(
                            item.getId(),
                            product.id(),
                            product.title(),
                            product.price(),
                            product.productType(),
                            null,
                            item.getQuantity(),
                            subtotal
                    );
                })
                .toList();
    }

    private @NonNull CartCheckoutEvent toCheckoutEvent(Cart cart) {
        return new CartCheckoutEvent(
                cart.getId(),
                cart.getUserId(),
                cart.getItems().stream()
                        .map(this::toCartItemDto)
                        .toList()
        );
    }

    private @NonNull CartItemDto toCartItemDto(CartItem cartItem) {
        return new CartItemDto(
                cartItem.getId(),
                cartItem.getProductId(),
                cartItem.getQuantity()
        );
    }
}
