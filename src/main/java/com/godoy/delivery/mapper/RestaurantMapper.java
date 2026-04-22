package com.godoy.delivery.mapper;

import com.godoy.delivery.domain.entity.Restaurant;
import com.godoy.delivery.dto.request.RestaurantRequest;
import com.godoy.delivery.dto.response.RestaurantResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RestaurantMapper {

    Restaurant toEntity(RestaurantRequest request);

    RestaurantResponse toResponse(Restaurant restaurant);
}
