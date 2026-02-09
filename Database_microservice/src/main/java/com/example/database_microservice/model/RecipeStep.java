package com.example.database_microservice.model;

import com.example.database_microservice.DTOs.RecipeStepDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "recipe_step")
public class RecipeStep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    @JsonIgnore
    private Recipe recipe;

    @Column(name = "step_number", nullable = false)
    private Integer stepNumber;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String instruction;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    public RecipeStep() {}

    public RecipeStep(RecipeStepDTO dto) {
        this.id = dto.getId();
        this.stepNumber = dto.getStepNumber();
        this.instruction = dto.getInstruction();
        this.durationMinutes = dto.getDurationMinutes();
    }
}
