package com.example.list_microservice.DTOs;

import lombok.Data;

@Data
public class ListItemDTO {
    private Long id;
    private Long listId;
    private String textOfItem;
    private Long productId;
    private String productName;
    private Double quantity;
    private String unit;
    private Long sourceMealPlanId;
    private Long sourceRecipeId;
    private String sourceRecipeName;
    private ListItemType itemType;
    private boolean done;
    private Integer sortOrder;
}
