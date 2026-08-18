package com.example.products.controller;

import com.example.products.dto.ProductSummaryDto;
import com.example.products.dto.GameDto;
import com.example.products.entity.games.Genre;
import com.example.products.entity.games.Platform;
import com.example.products.service.GameService;
import com.example.products.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final GameService gameService;
    private final ProductService productService;

    @GetMapping("/games")
    public ResponseEntity<Page<GameDto>> getGames(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Platform platform,
            @RequestParam(required = false) Genre genre,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            Pageable pageable
    ) {
        Page<GameDto> result = gameService.getGamesFiltered(
                title,
                platform,
                genre,
                minPrice,
                maxPrice,
                pageable
        );

        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/games/{id}")
    public ResponseEntity<GameDto> getGameById(@PathVariable String id) {
        GameDto result =  gameService.getGameById(id);

        if (result == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(result);
    }

    @PostMapping("/batch")
    public ResponseEntity<List<ProductSummaryDto>> getProductSummaries(@RequestBody List<String> productIds) {
        List<ProductSummaryDto> result = productService.getProductSummariesByIds(productIds);

        return ResponseEntity.ok(result);
    }
}

