package com.example.product_microservice.repository;

import com.example.product_microservice.DTOs.ProductDTO;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepository {

    private final RestTemplate restTemplate;
    private final String externalBase = "http://dbmicroservice:9009/db/groceryProduct";

    public ProductRepository(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public List<ProductDTO> getAllProducts() {
        try {
            ResponseEntity<ProductDTO[]> response = restTemplate.getForEntity(externalBase, ProductDTO[].class);
            ProductDTO[] body = response.getBody();
            return Arrays.asList(Optional.ofNullable(body).orElse(new ProductDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    public ResponseEntity<ProductDTO> getProductById(Long id) {
        try {
            ResponseEntity<ProductDTO> response =
                    restTemplate.getForEntity(externalBase + "/" + id, ProductDTO.class);
            return ResponseEntity.ok(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<ProductDTO> createProduct(ProductDTO productDTO) {
        try {
            ResponseEntity<ProductDTO> response =
                    restTemplate.postForEntity(externalBase, productDTO, ProductDTO.class);
            return ResponseEntity.status(HttpStatus.CREATED).body(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    public ResponseEntity<ProductDTO> updateProduct(Long id, ProductDTO updatedProductDTO) {
        try {
            restTemplate.put(externalBase + "/" + id, updatedProductDTO);
            return ResponseEntity.ok(updatedProductDTO);
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Void> deleteProduct(Long id) {
        try {
            restTemplate.delete(externalBase + "/" + id);
            return ResponseEntity.noContent().build();
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
