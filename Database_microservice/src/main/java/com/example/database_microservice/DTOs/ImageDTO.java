package com.example.database_microservice.DTOs;

import com.example.database_microservice.model.EntityType;
import com.example.database_microservice.model.Image;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ImageDTO {
    private Long id;
    private String fileName;
    private String originalFileName;
    private String contentType;
    private Long fileSize;
    private String storageKey;
    private EntityType entityType;
    private Long entityId;
    private LocalDateTime uploadedAt;
    private Long uploadedBy;

    public ImageDTO() {}

    public ImageDTO(Image image) {
        this.id = image.getId();
        this.fileName = image.getFileName();
        this.originalFileName = image.getOriginalFileName();
        this.contentType = image.getContentType();
        this.fileSize = image.getFileSize();
        this.storageKey = image.getStorageKey();
        this.entityType = image.getEntityType();
        this.entityId = image.getEntityId();
        this.uploadedAt = image.getUploadedAt();
        this.uploadedBy = image.getUploadedBy();
    }
}
