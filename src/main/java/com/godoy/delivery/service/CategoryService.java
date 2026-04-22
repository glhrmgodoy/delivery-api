package com.godoy.delivery.service;

import com.godoy.delivery.domain.entity.Category;
import com.godoy.delivery.dto.request.CategoryRequest;
import com.godoy.delivery.dto.response.CategoryResponse;
import com.godoy.delivery.exception.BusinessException;
import com.godoy.delivery.exception.NotFoundException;
import com.godoy.delivery.mapper.CategoryMapper;
import com.godoy.delivery.repository.CategoryRepository;
import com.godoy.delivery.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CategoryMapper categoryMapper;

    public CategoryResponse create(CategoryRequest request) {
        Category category = categoryMapper.toEntity(request);
        Category saved =  categoryRepository.save(category);
        return categoryMapper.toResponse(saved);
    }

    public List<CategoryResponse> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    public CategoryResponse findById(UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada"));
        return categoryMapper.toResponse(category);
    }

    public CategoryResponse update(UUID id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada"));

        category.setName(request.name());
        category.setDescription(request.description());

        Category updated = categoryRepository.save(category);
        return categoryMapper.toResponse(updated);
    }

    public void delete(UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada"));

        if (productRepository.existsByCategoryIdAndActiveTrue(id)) {
            throw new BusinessException("Categoria possui produtos ativos e não pode ser inativa");
        }

        category.setActive(false);
        categoryRepository.save(category);
    }
}
