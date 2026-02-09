package com.example.database_microservice.service;

import com.example.database_microservice.DTOs.ProductDTO;
import com.example.database_microservice.model.Product;
import com.example.database_microservice.repository.ProductCategoryRepository;
import com.example.database_microservice.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductCategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, ProductCategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    private ProductDTO toDTO(Product product) {
        return new ProductDTO(product);
    }

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream().map(this::toDTO).toList();
    }

    public Optional<ProductDTO> getProductById(Long id) {
        return productRepository.findById(id).map(this::toDTO);
    }

    public List<ProductDTO> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId)
                .stream().map(this::toDTO).toList();
    }

    public List<ProductDTO> searchProducts(String name) {
        return productRepository.findByNameOfProductContainingIgnoreCase(name)
                .stream().map(this::toDTO).toList();
    }

    public List<ProductDTO> searchProductsByCategory(Long categoryId, String name) {
        return productRepository.findByCategoryIdAndNameOfProductContainingIgnoreCase(categoryId, name)
                .stream().map(this::toDTO).toList();
    }

    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = new Product(productDTO);
        if (productDTO.getCategoryId() != null) {
            categoryRepository.findById(productDTO.getCategoryId())
                    .ifPresent(product::setCategory);
        }
        return toDTO(productRepository.save(product));
    }

    public Optional<ProductDTO> updateProduct(Long id, ProductDTO updatedProductDTO) {
        return productRepository.findById(id).map(product -> {
            product.setNameOfProduct(updatedProductDTO.getNameOfProduct());
            product.setDescription(updatedProductDTO.getDescription());
            if (updatedProductDTO.getCategoryId() != null) {
                categoryRepository.findById(updatedProductDTO.getCategoryId())
                        .ifPresent(product::setCategory);
            } else {
                product.setCategory(null);
            }
            return toDTO(productRepository.save(product));
        });
    }

    public boolean deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
