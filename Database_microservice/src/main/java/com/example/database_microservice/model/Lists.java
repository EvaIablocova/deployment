package com.example.database_microservice.model;

import com.example.database_microservice.DTOs.ListDTO;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "lists")
public class Lists {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "created_by")
    private Long createdBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "list_type", length = 20)
    private ListType listType = ListType.SHOPPING;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "list", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ListItem> items = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Lists(){}

    public Lists(ListDTO listDTO){
        this.id = listDTO.getId();
        this.title = listDTO.getTitle();
        this.description = listDTO.getDescription();
        this.groupId = listDTO.getGroupId();
        this.createdBy = listDTO.getCreatedBy();
        this.listType = listDTO.getListType() != null ? listDTO.getListType() : ListType.SHOPPING;
    }

}