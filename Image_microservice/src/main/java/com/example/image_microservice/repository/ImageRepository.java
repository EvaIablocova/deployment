package com.example.image_microservice.repository;

import com.example.image_microservice.DTOs.EntityType;
import com.example.image_microservice.DTOs.ImageDTO;
import org.springframework.beans.factory.annotation.Value;
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
public class ImageRepository {

    private final RestTemplate restTemplate;
    private final String externalBase;

    public ImageRepository(RestTemplateBuilder builder,
                           @Value("${service.database.url}") String databaseUrl) {
        this.restTemplate = builder.build();
        this.externalBase = databaseUrl + "/db/images";
    }

    public List<ImageDTO> getAllImages() {
        try {
            ResponseEntity<ImageDTO[]> response = restTemplate.getForEntity(externalBase, ImageDTO[].class);
            ImageDTO[] body = response.getBody();
            return Arrays.asList(Optional.ofNullable(body).orElse(new ImageDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    public ResponseEntity<ImageDTO> getImageById(Long id) {
        try {
            ResponseEntity<ImageDTO> response =
                    restTemplate.getForEntity(externalBase + "/" + id, ImageDTO.class);
            return ResponseEntity.ok(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public List<ImageDTO> getImagesByEntity(EntityType entityType, Long entityId) {
        try {
            ResponseEntity<ImageDTO[]> response = restTemplate.getForEntity(
                    externalBase + "/entity/" + entityType + "/" + entityId, ImageDTO[].class);
            ImageDTO[] body = response.getBody();
            return Arrays.asList(Optional.ofNullable(body).orElse(new ImageDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    public ResponseEntity<ImageDTO> createImage(ImageDTO imageDTO) {
        try {
            ResponseEntity<ImageDTO> response =
                    restTemplate.postForEntity(externalBase, imageDTO, ImageDTO.class);
            return ResponseEntity.status(HttpStatus.CREATED).body(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    public ResponseEntity<Void> deleteImage(Long id) {
        try {
            restTemplate.delete(externalBase + "/" + id);
            return ResponseEntity.noContent().build();
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
