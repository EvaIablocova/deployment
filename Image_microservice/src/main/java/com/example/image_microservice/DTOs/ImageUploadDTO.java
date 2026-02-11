package com.example.image_microservice.DTOs;

import lombok.Data;

@Data
public class ImageUploadDTO {
    private EntityType entityType;
    private Long entityId;
    private Long uploadedBy;

    public ImageUploadDTO() {}
}
