package com.example.product_microservice.repository;

import com.example.product_microservice.DTOs.ProductCategoryDTO;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class ProductCategoryRepository {

    private final RestTemplate restTemplate;
    private final String externalBase = "http://dbmicroservice:9009/db/product-categories";

    public ProductCategoryRepository(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public List<ProductCategoryDTO> getAllCategories() {
        try {
            ResponseEntity<ProductCategoryDTO[]> response = restTemplate.getForEntity(externalBase, ProductCategoryDTO[].class);
            ProductCategoryDTO[] body = response.getBody();
            return Arrays.asList(Optional.ofNullable(body).orElse(new ProductCategoryDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    public ResponseEntity<ProductCategoryDTO> getCategoryById(Long id) {
        try {
            ResponseEntity<ProductCategoryDTO> response =
                    restTemplate.getForEntity(externalBase + "/" + id, ProductCategoryDTO.class);
            return ResponseEntity.ok(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<ProductCategoryDTO> getCategoryByName(String name) {
        try {
            ResponseEntity<ProductCategoryDTO> response =
                    restTemplate.getForEntity(externalBase + "/name/" + name, ProductCategoryDTO.class);
            return ResponseEntity.ok(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<?> createCategory(ProductCategoryDTO categoryDTO) {
        try {
            ResponseEntity<ProductCategoryDTO> response =
                    restTemplate.postForEntity(externalBase, categoryDTO, ProductCategoryDTO.class);
            return ResponseEntity.status(HttpStatus.CREATED).body(response.getBody());
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (RestClientException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Failed to create category"));
        }
    }

    public ResponseEntity<?> updateCategory(Long id, ProductCategoryDTO categoryDTO) {
        try {
            restTemplate.put(externalBase + "/" + id, categoryDTO);
            return ResponseEntity.ok(categoryDTO);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Void> deleteCategory(Long id) {
        try {
            restTemplate.delete(externalBase + "/" + id);
            return ResponseEntity.noContent().build();
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
