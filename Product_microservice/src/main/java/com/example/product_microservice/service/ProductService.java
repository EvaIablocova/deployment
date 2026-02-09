package com.example.product_microservice.service;

import com.example.product_microservice.DTOs.ProductDTO;
import com.example.product_microservice.repository.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductDTO> getAllProducts() {
        return productRepository.getAllProducts();
    }

    public Optional<ProductDTO> getProductById(Long id) {
        return Optional.ofNullable(productRepository.getProductById(id))
                .map(ResponseEntity::getBody);
    }

    public List<ProductDTO> getProductsByCategory(Long categoryId) {
        return productRepository.getProductsByCategory(categoryId);
    }

    public List<ProductDTO> searchProducts(String name, Long categoryId) {
        return productRepository.searchProducts(name, categoryId);
    }

    public ProductDTO createProduct(ProductDTO productDTO) {
        return productRepository.createProduct(productDTO).getBody();
    }

    public ResponseEntity<ProductDTO> updateProduct(Long id, ProductDTO updatedProductDTO) {
        return productRepository.updateProduct(id, updatedProductDTO);
    }

    public ResponseEntity<Void> deleteProduct(Long id) {
        return productRepository.deleteProduct(id);
    }
}
