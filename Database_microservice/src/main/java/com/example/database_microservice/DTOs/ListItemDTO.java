package com.example.database_microservice.DTOs;

import com.example.database_microservice.model.ListItem;
import com.example.database_microservice.model.ListItemType;
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

    public ListItemDTO() { }

    public ListItemDTO(ListItem listItem){
        this.id = listItem.getId();
        this.listId = listItem.getList() != null ? listItem.getList().getId() : null;
        this.textOfItem = listItem.getTextOfItem();
        if (listItem.getProduct() != null) {
            this.productId = listItem.getProduct().getId();
            this.productName = listItem.getProduct().getNameOfProduct();
        }
        this.quantity = listItem.getQuantity();
        this.unit = listItem.getUnit();
        this.sourceMealPlanId = listItem.getSourceMealPlanId();
        this.sourceRecipeId = listItem.getSourceRecipeId();
        this.itemType = listItem.getItemType();
        this.done = listItem.isDone();
        this.sortOrder = listItem.getSortOrder();
    }

}