package com.example.api_gateway.Controller;

import com.example.api_gateway.DTOs.ImageDTO;
import com.example.api_gateway.config.ServiceUrlsConfig;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api_gateway/products")
public class ProductImageController {

    private final RestTemplate restTemplate;
    private final String externalBase;

    public ProductImageController(RestTemplateBuilder builder, ServiceUrlsConfig serviceUrls) {
        this.restTemplate = builder.build();
        this.externalBase = serviceUrls.getProductServiceUrl() + "/api/products";
    }

    @GetMapping("/{productId}/images")
    public ResponseEntity<List<ImageDTO>> getProductImages(@PathVariable Long productId) {
        try {
            ResponseEntity<ImageDTO[]> response = restTemplate.getForEntity(
                    externalBase + "/" + productId + "/images", ImageDTO[].class);
            ImageDTO[] body = response.getBody();
            return ResponseEntity.ok(Arrays.asList(Optional.ofNullable(body).orElse(new ImageDTO[0])));
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{productId}/images/{imageId}")
    public ResponseEntity<ImageDTO> getProductImage(
            @PathVariable Long productId,
            @PathVariable Long imageId) {
        try {
            ResponseEntity<ImageDTO> response = restTemplate.getForEntity(
                    externalBase + "/" + productId + "/images/" + imageId, ImageDTO.class);
            return ResponseEntity.ok(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/{productId}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageDTO> uploadProductImage(
            @PathVariable Long productId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "uploadedBy", required = false) Long uploadedBy) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new MultipartFileResource(file));
            if (uploadedBy != null) {
                body.add("uploadedBy", uploadedBy.toString());
            }

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<ImageDTO> response = restTemplate.postForEntity(
                    externalBase + "/" + productId + "/images", requestEntity, ImageDTO.class);
            return ResponseEntity.status(HttpStatus.CREATED).body(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/{productId}/images/{imageId}/download")
    public ResponseEntity<byte[]> downloadProductImage(
            @PathVariable Long productId,
            @PathVariable Long imageId) {
        try {
            ResponseEntity<byte[]> response = restTemplate.getForEntity(
                    externalBase + "/" + productId + "/images/" + imageId + "/download", byte[].class);
            return response;
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{productId}/images/{imageId}")
    public ResponseEntity<Void> deleteProductImage(
            @PathVariable Long productId,
            @PathVariable Long imageId) {
        try {
            restTemplate.delete(externalBase + "/" + productId + "/images/" + imageId);
            return ResponseEntity.noContent().build();
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private static class MultipartFileResource extends ByteArrayResource {
        private final String filename;

        public MultipartFileResource(MultipartFile file) {
            super(getBytes(file));
            this.filename = file.getOriginalFilename();
        }

        private static byte[] getBytes(MultipartFile file) {
            try {
                return file.getBytes();
            } catch (IOException e) {
                throw new RuntimeException("Failed to read file bytes", e);
            }
        }

        @Override
        public String getFilename() {
            return this.filename;
        }
    }
}
