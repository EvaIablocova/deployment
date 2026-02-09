package com.example.database_microservice.service;

import com.example.database_microservice.DTOs.ProductCategoryDTO;
import com.example.database_microservice.model.ProductCategory;
import com.example.database_microservice.repository.ProductCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductCategoryService {
    private final ProductCategoryRepository categoryRepository;

    public ProductCategoryService(ProductCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    private ProductCategoryDTO toDTO(ProductCategory category) {
        return new ProductCategoryDTO(category);
    }

    public List<ProductCategoryDTO> getAllCategories() {
        return categoryRepository.findAllByOrderByDisplayOrderAsc()
                .stream().map(this::toDTO).toList();
    }

    public Optional<ProductCategoryDTO> getCategoryById(Long id) {
        return categoryRepository.findById(id).map(this::toDTO);
    }

    public Optional<ProductCategoryDTO> getCategoryByName(String name) {
        return categoryRepository.findByName(name).map(this::toDTO);
    }

    public ProductCategoryDTO createCategory(ProductCategoryDTO categoryDTO) {
        if (categoryRepository.existsByName(categoryDTO.getName())) {
            throw new IllegalArgumentException("Category with name '" + categoryDTO.getName() + "' already exists");
        }
        ProductCategory category = new ProductCategory(categoryDTO);
        return toDTO(categoryRepository.save(category));
    }

    public Optional<ProductCategoryDTO> updateCategory(Long id, ProductCategoryDTO categoryDTO) {
        return categoryRepository.findById(id).map(category -> {
            if (!category.getName().equals(categoryDTO.getName())
                    && categoryRepository.existsByName(categoryDTO.getName())) {
                throw new IllegalArgumentException("Category with name '" + categoryDTO.getName() + "' already exists");
            }
            category.setName(categoryDTO.getName());
            category.setDescription(categoryDTO.getDescription());
            category.setIcon(categoryDTO.getIcon());
            category.setDisplayOrder(categoryDTO.getDisplayOrder());
            return toDTO(categoryRepository.save(category));
        });
    }

    public boolean deleteCategory(Long id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
