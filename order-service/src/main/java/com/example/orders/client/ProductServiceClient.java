package com.example.orders.client;

import com.example.orders.dto.ProductSummaryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "product-service", url = "${product.service.url}")
public interface ProductServiceClient {

    @PostMapping("/api/products/batch")
    List<ProductSummaryDto> getProductsByIds(@RequestBody List<String> productIds);
}
