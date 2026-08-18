package com.example.cart.client;

import com.example.cart.dto.CartProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "product-service", url = "${product.service.url}")
public interface ProductServiceClient {

    @PostMapping("/api/products/batch")
    List<CartProductDto> getProductsByIds(@RequestBody List<String> productIds);
}
