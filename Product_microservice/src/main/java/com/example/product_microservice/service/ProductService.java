package com.example.product_microservice.service;

import com.example.product_microservice.DTOs.ImageDTO;
import com.example.product_microservice.DTOs.ProductDTO;
import com.example.product_microservice.repository.ImageRepository;
import com.example.product_microservice.repository.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;

    public ProductService(ProductRepository productRepository, ImageRepository imageRepository) {
        this.productRepository = productRepository;
        this.imageRepository = imageRepository;
    }

    public List<ProductDTO> getAllProducts() {
        List<ProductDTO> products = productRepository.getAllProducts();
        products.forEach(this::enrichWithImages);
        return products;
    }

    public Optional<ProductDTO> getProductById(Long id) {
        return Optional.ofNullable(productRepository.getProductById(id))
                .map(ResponseEntity::getBody)
                .map(this::enrichWithImages);
    }

    public List<ProductDTO> getProductsByCategory(Long categoryId) {
        List<ProductDTO> products = productRepository.getProductsByCategory(categoryId);
        products.forEach(this::enrichWithImages);
        return products;
    }

    public List<ProductDTO> searchProducts(String name, Long categoryId) {
        List<ProductDTO> products = productRepository.searchProducts(name, categoryId);
        products.forEach(this::enrichWithImages);
        return products;
    }

    public ProductDTO createProduct(ProductDTO productDTO) {
        ProductDTO created = productRepository.createProduct(productDTO).getBody();
        return enrichWithImages(created);
    }

    public ResponseEntity<ProductDTO> updateProduct(Long id, ProductDTO updatedProductDTO) {
        ResponseEntity<ProductDTO> response = productRepository.updateProduct(id, updatedProductDTO);
        if (response.getBody() != null) {
            enrichWithImages(response.getBody());
        }
        return response;
    }

    public ResponseEntity<Void> deleteProduct(Long id) {
        List<ImageDTO> images = imageRepository.getImagesByProduct(id);
        for (ImageDTO image : images) {
            imageRepository.deleteImage(image.getId());
        }
        return productRepository.deleteProduct(id);
    }

    private ProductDTO enrichWithImages(ProductDTO product) {
        if (product != null && product.getId() != null) {
            List<ImageDTO> images = imageRepository.getImagesByProduct(product.getId());
            product.setImages(images);
            if (!images.isEmpty()) {
                product.setImageId(images.get(0).getId());
            }
        }
        return product;
    }
}
