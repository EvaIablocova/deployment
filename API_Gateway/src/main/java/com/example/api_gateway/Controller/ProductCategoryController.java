package com.example.api_gateway.Controller;

import com.example.api_gateway.DTOs.ProductCategoryDTO;
import com.example.api_gateway.config.ServiceUrlsConfig;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api_gateway/product-categories")
public class ProductCategoryController {

    private final RestTemplate restTemplate;
    private final String externalBase;

    public ProductCategoryController(RestTemplateBuilder builder, ServiceUrlsConfig serviceUrls) {
        this.restTemplate = builder.build();
        this.externalBase = serviceUrls.getProductServiceUrl() + "/api/product-categories";
    }

    @GetMapping
    public List<ProductCategoryDTO> getAllCategories() {
        try {
            ResponseEntity<ProductCategoryDTO[]> response = restTemplate.getForEntity(externalBase, ProductCategoryDTO[].class);
            ProductCategoryDTO[] body = response.getBody();
            return Arrays.asList(Optional.ofNullable(body).orElse(new ProductCategoryDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductCategoryDTO> getCategoryById(@PathVariable Long id) {
        try {
            ResponseEntity<ProductCategoryDTO> response =
                    restTemplate.getForEntity(externalBase + "/" + id, ProductCategoryDTO.class);
            return ResponseEntity.ok(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ProductCategoryDTO> getCategoryByName(@PathVariable String name) {
        try {
            ResponseEntity<ProductCategoryDTO> response =
                    restTemplate.getForEntity(externalBase + "/name/" + name, ProductCategoryDTO.class);
            return ResponseEntity.ok(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody ProductCategoryDTO categoryDTO) {
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

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody ProductCategoryDTO categoryDTO) {
        try {
            restTemplate.put(externalBase + "/" + id, categoryDTO);
            return ResponseEntity.ok(categoryDTO);
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        try {
            restTemplate.delete(externalBase + "/" + id);
            return ResponseEntity.noContent().build();
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
