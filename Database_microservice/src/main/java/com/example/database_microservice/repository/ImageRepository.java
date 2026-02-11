package com.example.database_microservice.repository;

import com.example.database_microservice.model.EntityType;
import com.example.database_microservice.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByEntityTypeAndEntityId(EntityType entityType, Long entityId);
    List<Image> findByEntityType(EntityType entityType);
    List<Image> findByUploadedBy(Long uploadedBy);
}
