package com.godoy.delivery.mapper;

import com.godoy.delivery.domain.entity.Category;
import com.godoy.delivery.dto.request.CategoryRequest;
import com.godoy.delivery.dto.response.CategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {

    Category toEntity(CategoryRequest request);

    CategoryResponse toResponse(Category category);
}
