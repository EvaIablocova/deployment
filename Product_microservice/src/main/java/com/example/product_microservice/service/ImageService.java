package com.example.product_microservice.service;

import com.example.product_microservice.DTOs.ImageDTO;
import com.example.product_microservice.repository.ImageRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class ImageService {

    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public List<ImageDTO> getImagesByProduct(Long productId) {
        return imageRepository.getImagesByProduct(productId);
    }

    public Optional<ImageDTO> getImageById(Long id) {
        return Optional.ofNullable(imageRepository.getImageById(id))
                .map(ResponseEntity::getBody);
    }

    public ResponseEntity<ImageDTO> uploadImage(MultipartFile file, Long productId, Long uploadedBy) {
        return imageRepository.uploadImage(file, productId, uploadedBy);
    }

    public ResponseEntity<byte[]> downloadImage(Long id) {
        return imageRepository.downloadImage(id);
    }

    public ResponseEntity<Void> deleteImage(Long id) {
        return imageRepository.deleteImage(id);
    }
}
