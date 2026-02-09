package com.example.database_microservice.model;

import com.example.database_microservice.DTOs.RecipeIngredientDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "recipe_ingredient")
public class RecipeIngredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    @JsonIgnore
    private Recipe recipe;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(length = 150)
    private String customIngredientName;

    @Column(nullable = false)
    private Double quantity;

    @Column(length = 50)
    private String unit;

    @Column(columnDefinition = "TEXT")
    private String notes;

    public RecipeIngredient() {}

    public RecipeIngredient(RecipeIngredientDTO dto) {
        this.id = dto.getId();
        this.quantity = dto.getQuantity();
        this.unit = dto.getUnit();
        this.notes = dto.getNotes();
        this.customIngredientName = dto.getCustomIngredientName();
    }

    public String getIngredientName() {
        if (product != null) {
            return product.getNameOfProduct();
        }
        return customIngredientName;
    }

    public boolean isCustomIngredient() {
        return product == null && customIngredientName != null;
    }
}
