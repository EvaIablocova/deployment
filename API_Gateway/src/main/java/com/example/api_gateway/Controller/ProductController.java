package com.example.api_gateway.Controller;

import com.example.api_gateway.DTOs.ProductDTO;
import com.example.api_gateway.config.ServiceUrlsConfig;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api_gateway/products")
public class ProductController {

    private final RestTemplate restTemplate;
    private final String externalBase;

    public ProductController(RestTemplateBuilder builder, ServiceUrlsConfig serviceUrls) {
        this.restTemplate = builder.build();
        this.externalBase = serviceUrls.getProductServiceUrl() + "/api/products";
    }

    // --- CRUD ---

    @GetMapping
    public List<ProductDTO> getAllProducts() {
        try {
            ResponseEntity<ProductDTO[]> response = restTemplate.getForEntity(externalBase, ProductDTO[].class);
            ProductDTO[] body = response.getBody();
            return Arrays.asList(Optional.ofNullable(body).orElse(new ProductDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        try {
            ResponseEntity<ProductDTO> response =
                    restTemplate.getForEntity(externalBase + "/" + id, ProductDTO.class);
            return ResponseEntity.ok(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        try {
            ResponseEntity<ProductDTO> response =
                    restTemplate.postForEntity(externalBase, productDTO, ProductDTO.class);
            return ResponseEntity.status(HttpStatus.CREATED).body(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO updatedProductDTO) {
        try {
            restTemplate.put(externalBase + "/" + id, updatedProductDTO);
            return ResponseEntity.ok(updatedProductDTO);
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        try {
            restTemplate.delete(externalBase + "/" + id);
            return ResponseEntity.noContent().build();
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
