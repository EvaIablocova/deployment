package com.example.database_microservice.service;

import com.example.database_microservice.DTOs.ImageDTO;
import com.example.database_microservice.model.EntityType;
import com.example.database_microservice.model.Image;
import com.example.database_microservice.repository.ImageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ImageService {
    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    private ImageDTO toDTO(Image image) {
        return new ImageDTO(image);
    }

    public List<ImageDTO> getAllImages() {
        return imageRepository.findAll().stream().map(this::toDTO).toList();
    }

    public Optional<ImageDTO> getImageById(Long id) {
        return imageRepository.findById(id).map(this::toDTO);
    }

    public List<ImageDTO> getImagesByEntity(EntityType entityType, Long entityId) {
        return imageRepository.findByEntityTypeAndEntityId(entityType, entityId)
                .stream().map(this::toDTO).toList();
    }

    public List<ImageDTO> getImagesByEntityType(EntityType entityType) {
        return imageRepository.findByEntityType(entityType)
                .stream().map(this::toDTO).toList();
    }

    public List<ImageDTO> getImagesByUploader(Long uploadedBy) {
        return imageRepository.findByUploadedBy(uploadedBy)
                .stream().map(this::toDTO).toList();
    }

    public ImageDTO createImage(ImageDTO imageDTO) {
        Image image = new Image(imageDTO);
        return toDTO(imageRepository.save(image));
    }

    public Optional<ImageDTO> updateImage(Long id, ImageDTO updatedImageDTO) {
        return imageRepository.findById(id).map(image -> {
            image.setFileName(updatedImageDTO.getFileName());
            image.setOriginalFileName(updatedImageDTO.getOriginalFileName());
            image.setContentType(updatedImageDTO.getContentType());
            image.setFileSize(updatedImageDTO.getFileSize());
            image.setStorageKey(updatedImageDTO.getStorageKey());
            image.setEntityType(updatedImageDTO.getEntityType());
            image.setEntityId(updatedImageDTO.getEntityId());
            image.setUploadedBy(updatedImageDTO.getUploadedBy());
            return toDTO(imageRepository.save(image));
        });
    }

    public boolean deleteImage(Long id) {
        if (imageRepository.existsById(id)) {
            imageRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
