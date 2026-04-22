package com.godoy.delivery.controller;

import com.godoy.delivery.domain.enums.ProductStatus;
import com.godoy.delivery.dto.request.ProductRequest;
import com.godoy.delivery.dto.response.ProductResponse;
import com.godoy.delivery.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> create(@RequestBody @Valid ProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAll() {
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<ProductResponse>> findAllByRestaurantId(@PathVariable UUID restaurantId) {
        return ResponseEntity.ok(productService.findAllByRestaurantId(restaurantId));
    }

    @GetMapping("/restaurant/{restaurantId}/available")
    public ResponseEntity<List<ProductResponse>> findAllAvailableByRestaurantId(@PathVariable UUID restaurantId) {
        return ResponseEntity.ok(productService.findAllAvailableByRestaurantId(restaurantId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(@PathVariable UUID id,
                                                  @RequestBody @Valid ProductRequest request) {
        return ResponseEntity.ok(productService.update(id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ProductResponse> updateStatus(@PathVariable UUID id,
                                                        @RequestParam ProductStatus status) {
        return ResponseEntity.ok(productService.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
