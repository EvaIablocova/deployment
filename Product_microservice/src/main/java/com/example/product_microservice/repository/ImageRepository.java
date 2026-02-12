package com.example.product_microservice.repository;

import com.example.product_microservice.DTOs.EntityType;
import com.example.product_microservice.DTOs.ImageDTO;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public class ImageRepository {

    private final RestTemplate restTemplate;
    private final String imageServiceBase = "http://imagemicroservice:9024/api/images";

    public ImageRepository(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public List<ImageDTO> getImagesByProduct(Long productId) {
        try {
            String url = imageServiceBase + "/entity/PRODUCT/" + productId;
            ResponseEntity<ImageDTO[]> response = restTemplate.getForEntity(url, ImageDTO[].class);
            ImageDTO[] body = response.getBody();
            return Arrays.asList(Optional.ofNullable(body).orElse(new ImageDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    public ResponseEntity<ImageDTO> getImageById(Long id) {
        try {
            ResponseEntity<ImageDTO> response = restTemplate.getForEntity(
                    imageServiceBase + "/" + id, ImageDTO.class);
            return ResponseEntity.ok(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<ImageDTO> uploadImage(MultipartFile file, Long productId, Long uploadedBy) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new MultipartFileResource(file));
            body.add("entityType", EntityType.PRODUCT.name());
            body.add("entityId", productId.toString());
            if (uploadedBy != null) {
                body.add("uploadedBy", uploadedBy.toString());
            }

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<ImageDTO> response = restTemplate.postForEntity(
                    imageServiceBase, requestEntity, ImageDTO.class);
            return ResponseEntity.status(HttpStatus.CREATED).body(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    public ResponseEntity<byte[]> downloadImage(Long id) {
        try {
            ResponseEntity<byte[]> response = restTemplate.getForEntity(
                    imageServiceBase + "/" + id + "/download", byte[].class);
            return response;
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Void> deleteImage(Long id) {
        try {
            restTemplate.delete(imageServiceBase + "/" + id);
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
