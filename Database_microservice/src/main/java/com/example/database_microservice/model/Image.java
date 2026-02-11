package com.example.database_microservice.model;

import com.example.database_microservice.DTOs.ImageDTO;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "image")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String originalFileName;

    @Column(nullable = false)
    private String contentType;

    @Column(nullable = false)
    private Long fileSize;

    @Column(nullable = false)
    private String storageKey;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EntityType entityType;

    @Column(nullable = false)
    private Long entityId;

    @Column(nullable = false)
    private LocalDateTime uploadedAt;

    private Long uploadedBy;

    public Image() {}

    public Image(ImageDTO imageDTO) {
        this.id = imageDTO.getId();
        this.fileName = imageDTO.getFileName();
        this.originalFileName = imageDTO.getOriginalFileName();
        this.contentType = imageDTO.getContentType();
        this.fileSize = imageDTO.getFileSize();
        this.storageKey = imageDTO.getStorageKey();
        this.entityType = imageDTO.getEntityType();
        this.entityId = imageDTO.getEntityId();
        this.uploadedAt = imageDTO.getUploadedAt();
        this.uploadedBy = imageDTO.getUploadedBy();
    }

    @PrePersist
    protected void onCreate() {
        if (uploadedAt == null) {
            uploadedAt = LocalDateTime.now();
        }
    }
}
