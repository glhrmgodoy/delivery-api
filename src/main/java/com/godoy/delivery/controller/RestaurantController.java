package com.godoy.delivery.controller;

import com.godoy.delivery.domain.enums.RestaurantStatus;
import com.godoy.delivery.dto.request.RestaurantRequest;
import com.godoy.delivery.dto.response.RestaurantResponse;
import com.godoy.delivery.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping
    public ResponseEntity<RestaurantResponse> create(@RequestBody @Valid RestaurantRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurantService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<RestaurantResponse>> findAll() {
        return ResponseEntity.ok(restaurantService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(restaurantService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantResponse> update(@PathVariable UUID id,
                                                     @RequestBody @Valid RestaurantRequest request) {
        return ResponseEntity.ok(restaurantService.update(id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<RestaurantResponse> updateStatus(@PathVariable UUID id,
                                                           @RequestParam RestaurantStatus status) {
        return ResponseEntity.ok(restaurantService.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        restaurantService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
