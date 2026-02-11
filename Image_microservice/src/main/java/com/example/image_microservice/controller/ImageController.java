package com.example.image_microservice.controller;

import com.example.image_microservice.DTOs.EntityType;
import com.example.image_microservice.DTOs.ImageDTO;
import com.example.image_microservice.DTOs.ImageUploadDTO;
import com.example.image_microservice.service.ImageService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api/images")
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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageDTO> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("entityType") EntityType entityType,
            @RequestParam("entityId") Long entityId,
            @RequestParam(value = "uploadedBy", required = false) Long uploadedBy) {

        ImageUploadDTO uploadDTO = new ImageUploadDTO();
        uploadDTO.setEntityType(entityType);
        uploadDTO.setEntityId(entityId);
        uploadDTO.setUploadedBy(uploadedBy);

        ImageDTO result = imageService.uploadImage(file, uploadDTO);
        if (result == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<InputStreamResource> downloadImage(@PathVariable Long id) {
        return imageService.getImageById(id)
                .map(imageDTO -> {
                    InputStream inputStream = imageService.downloadImage(id);
                    if (inputStream == null) {
                        return ResponseEntity.notFound().<InputStreamResource>build();
                    }
                    return ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_DISPOSITION,
                                    "attachment; filename=\"" + imageDTO.getOriginalFileName() + "\"")
                            .contentType(MediaType.parseMediaType(imageDTO.getContentType()))
                            .body(new InputStreamResource(inputStream));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id) {
        return imageService.deleteImage(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
