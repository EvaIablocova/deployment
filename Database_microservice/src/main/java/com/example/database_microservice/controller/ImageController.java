package com.example.database_microservice.controller;

import com.example.database_microservice.DTOs.ImageDTO;
import com.example.database_microservice.model.EntityType;
import com.example.database_microservice.service.ImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/db/images")
public class ImageController {
    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping
    public List<ImageDTO> getAllImages() {
        return imageService.getAllImages();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImageDTO> getImageById(@PathVariable Long id) {
        return imageService.getImageById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/entity/{entityType}/{entityId}")
    public List<ImageDTO> getImagesByEntity(
            @PathVariable EntityType entityType,
            @PathVariable Long entityId) {
        return imageService.getImagesByEntity(entityType, entityId);
    }

    @GetMapping("/entity/{entityType}")
    public List<ImageDTO> getImagesByEntityType(@PathVariable EntityType entityType) {
        return imageService.getImagesByEntityType(entityType);
    }

    @GetMapping("/uploader/{uploadedBy}")
    public List<ImageDTO> getImagesByUploader(@PathVariable Long uploadedBy) {
        return imageService.getImagesByUploader(uploadedBy);
    }

    @PostMapping
    public ImageDTO createImage(@RequestBody ImageDTO imageDTO) {
        return imageService.createImage(imageDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ImageDTO> updateImage(@PathVariable Long id, @RequestBody ImageDTO updatedImageDTO) {
        return imageService.updateImage(id, updatedImageDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id) {
        return imageService.deleteImage(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
