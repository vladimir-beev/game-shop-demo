package com.example.inventory.controller;

import com.example.inventory.dto.AvailabilityResponse;
import com.example.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/{productId}/availability")
    public ResponseEntity<AvailabilityResponse> getAvailability(@PathVariable String productId) {
        AvailabilityResponse availabilityResponse = inventoryService.getAvailability(productId);

        return ResponseEntity.ok(availabilityResponse);
    }
}

