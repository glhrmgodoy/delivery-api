package com.godoy.delivery.repository;

import com.godoy.delivery.domain.entity.Restaurant;
import com.godoy.delivery.domain.enums.RestaurantStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, UUID> {

    Optional<Restaurant> findByEmail(String email);

    Optional<Restaurant> findByCnpj(String cnpj);

    boolean existsByEmail(String email);

    boolean existsByCnpj(String cnpj);

    List<Restaurant> findAllByStatus(RestaurantStatus status);
}
