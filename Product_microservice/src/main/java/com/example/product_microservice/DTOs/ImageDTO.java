package com.example.product_microservice.DTOs;

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
}
