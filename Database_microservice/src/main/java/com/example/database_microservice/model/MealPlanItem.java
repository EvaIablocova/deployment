package com.example.database_microservice.model;

import com.example.database_microservice.DTOs.MealPlanItemDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "meal_plan_item")
public class MealPlanItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_plan_day_id", nullable = false)
    @JsonIgnore
    private MealPlanDay mealPlanDay;

    @Enumerated(EnumType.STRING)
    @Column(name = "meal_type", nullable = false)
    private MealType mealType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "custom_meal", length = 200)
    private String customMeal;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column
    private Integer servings;

    @Column(name = "sort_order")
    private Integer sortOrder = 0;

    public MealPlanItem() {}

    public MealPlanItem(MealPlanItemDTO dto) {
        this.id = dto.getId();
        this.mealType = dto.getMealType();
        this.customMeal = dto.getCustomMeal();
        this.notes = dto.getNotes();
        this.servings = dto.getServings();
        this.sortOrder = dto.getSortOrder();
    }

    public String getMealName() {
        if (recipe != null) {
            return recipe.getTitle();
        } else if (product != null) {
            return product.getNameOfProduct();
        }
        return customMeal;
    }
}
