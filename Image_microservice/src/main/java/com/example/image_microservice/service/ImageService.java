package com.example.image_microservice.service;

import com.example.image_microservice.DTOs.EntityType;
import com.example.image_microservice.DTOs.ImageDTO;
import com.example.image_microservice.DTOs.ImageUploadDTO;
import com.example.image_microservice.repository.ImageRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ImageService {

    private final ImageRepository imageRepository;
    private final MinioService minioService;

    public ImageService(ImageRepository imageRepository, MinioService minioService) {
        this.imageRepository = imageRepository;
        this.minioService = minioService;
    }

    public List<ImageDTO> getAllImages() {
        return imageRepository.getAllImages();
    }

    public Optional<ImageDTO> getImageById(Long id) {
        ResponseEntity<ImageDTO> response = imageRepository.getImageById(id);
        return Optional.ofNullable(response.getBody());
    }

    public List<ImageDTO> getImagesByEntity(EntityType entityType, Long entityId) {
        return imageRepository.getImagesByEntity(entityType, entityId);
    }

    public ImageDTO uploadImage(MultipartFile file, ImageUploadDTO uploadDTO) {
        String originalFileName = file.getOriginalFilename();
        String extension = getFileExtension(originalFileName);
        String uuid = UUID.randomUUID().toString();
        String fileName = uuid + (extension.isEmpty() ? "" : "." + extension);
        String storageKey = uploadDTO.getEntityType() + "/" + uploadDTO.getEntityId() + "/" + fileName;

        minioService.uploadFile(storageKey, file);

        ImageDTO imageDTO = new ImageDTO();
        imageDTO.setFileName(fileName);
        imageDTO.setOriginalFileName(originalFileName);
        imageDTO.setContentType(file.getContentType());
        imageDTO.setFileSize(file.getSize());
        imageDTO.setStorageKey(storageKey);
        imageDTO.setEntityType(uploadDTO.getEntityType());
        imageDTO.setEntityId(uploadDTO.getEntityId());
        imageDTO.setUploadedAt(LocalDateTime.now());
        imageDTO.setUploadedBy(uploadDTO.getUploadedBy());

        ResponseEntity<ImageDTO> response = imageRepository.createImage(imageDTO);
        return response.getBody();
    }

    public InputStream downloadImage(Long id) {
        ResponseEntity<ImageDTO> response = imageRepository.getImageById(id);
        if (response.getBody() == null) {
            return null;
        }
        return minioService.downloadFile(response.getBody().getStorageKey());
    }

    public boolean deleteImage(Long id) {
        ResponseEntity<ImageDTO> response = imageRepository.getImageById(id);
        if (response.getBody() == null) {
            return false;
        }

        ImageDTO imageDTO = response.getBody();
        minioService.deleteFile(imageDTO.getStorageKey());

        ResponseEntity<Void> deleteResponse = imageRepository.deleteImage(id);
        return deleteResponse.getStatusCode().is2xxSuccessful();
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf('.') == -1) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }
}
