package com.godoy.delivery.mapper;

import com.godoy.delivery.domain.entity.Product;
import com.godoy.delivery.dto.request.ProductRequest;
import com.godoy.delivery.dto.response.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {

    @Mapping(target = "restaurant", ignore = true)
    @Mapping(target = "category", ignore = true)
    Product toEntity(ProductRequest request);

    @Mapping(source = "restaurant.name", target = "restaurantName")
    @Mapping(source = "category.name", target = "categoryName")
    ProductResponse toResponse(Product product);
}
