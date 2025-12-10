package com.example.database_microservice.model;

import com.example.database_microservice.DTOs.ListDTO;
import jakarta.persistence.*;
import lombok.Data;

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

    public Lists(){}

    public Lists(ListDTO listDTO){
        this.id = listDTO.getId();
        this.title = listDTO.getTitle();
        this.description = listDTO.getDescription();
    }

}