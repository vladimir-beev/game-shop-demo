package com.example.products.service;

import com.example.products.dto.ProductSummaryDto;

import java.util.List;

public interface ProductService {
    List<ProductSummaryDto> getProductSummariesByIds(List<String> productIds);
}
