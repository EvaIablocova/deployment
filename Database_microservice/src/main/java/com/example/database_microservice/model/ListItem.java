package com.example.database_microservice.model;

import com.example.database_microservice.DTOs.ListItemDTO;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "list_item")
public class ListItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "list_id", nullable = false)
    private Lists list;

    // For custom items - just text description
    @Column(length = 255)
    private String textOfItem;

    // Reference to Product from catalog (optional)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    // Quantity and unit for measurable items
    @Column
    private Double quantity;

    @Column(length = 50)
    private String unit;

    // Track source if item came from a meal plan
    @Column(name = "source_meal_plan_id")
    private Long sourceMealPlanId;

    // Track which recipe the ingredient came from
    @Column(name = "source_recipe_id")
    private Long sourceRecipeId;

    // Item type: PRODUCT, CUSTOM, RECIPE_INGREDIENT
    @Enumerated(EnumType.STRING)
    @Column(name = "item_type", length = 20)
    private ListItemType itemType = ListItemType.CUSTOM;

    @Column(name = "done", nullable = false)
    private boolean done = false;

    @Column(name = "sort_order")
    private Integer sortOrder = 0;

    public ListItem(){}

    public ListItem(ListItemDTO dto, Lists list){
        this.id = dto.getId();
        this.list = list;
        this.textOfItem = dto.getTextOfItem();
        this.quantity = dto.getQuantity();
        this.unit = dto.getUnit();
        this.sourceMealPlanId = dto.getSourceMealPlanId();
        this.sourceRecipeId = dto.getSourceRecipeId();
        this.itemType = dto.getItemType() != null ? dto.getItemType() : ListItemType.CUSTOM;
        this.done = dto.isDone();
        this.sortOrder = dto.getSortOrder();
    }

}