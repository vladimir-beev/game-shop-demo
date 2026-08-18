package com.example.products.service;

import com.example.products.dto.ProductSummaryDto;
import com.example.products.entity.Product;
import com.example.products.entity.games.Game;
import com.example.products.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public List<ProductSummaryDto> getProductSummariesByIds(List<String> productIds) {

        List<Product> products = productRepository.findAllById(productIds);

        return products.stream()
                .map(this::toProductSummaryDto)
                .toList();
    }

    private ProductSummaryDto toProductSummaryDto(Product product) {

        if (product instanceof Game) {

            String gamePlatform = ((Game) product).getPlatform().name();

            return  new ProductSummaryDto(
                    product.getId(),
                    product.getTitle(),
                    product.getPrice(),
                    "GAME",
                    gamePlatform
            );
        }

        throw new IllegalStateException(
                "Unknown product type: " + product.getClass().getName()
        );
    }
}
