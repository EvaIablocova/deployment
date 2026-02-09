package com.example.product_microservice.service;

import com.example.product_microservice.DTOs.ProductCategoryDTO;
import com.example.product_microservice.repository.ProductCategoryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductCategoryService {

    private final ProductCategoryRepository categoryRepository;

    public ProductCategoryService(ProductCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<ProductCategoryDTO> getAllCategories() {
        return categoryRepository.getAllCategories();
    }

    public Optional<ProductCategoryDTO> getCategoryById(Long id) {
        ResponseEntity<ProductCategoryDTO> response = categoryRepository.getCategoryById(id);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return Optional.of(response.getBody());
        }
        return Optional.empty();
    }

    public Optional<ProductCategoryDTO> getCategoryByName(String name) {
        ResponseEntity<ProductCategoryDTO> response = categoryRepository.getCategoryByName(name);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return Optional.of(response.getBody());
        }
        return Optional.empty();
    }

    public ResponseEntity<?> createCategory(ProductCategoryDTO categoryDTO) {
        return categoryRepository.createCategory(categoryDTO);
    }

    public ResponseEntity<?> updateCategory(Long id, ProductCategoryDTO categoryDTO) {
        return categoryRepository.updateCategory(id, categoryDTO);
    }

    public ResponseEntity<Void> deleteCategory(Long id) {
        return categoryRepository.deleteCategory(id);
    }
}
