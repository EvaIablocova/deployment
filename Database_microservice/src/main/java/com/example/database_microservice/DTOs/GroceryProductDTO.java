package com.example.database_microservice.DTOs;

import com.example.database_microservice.model.GroceryProduct;
import jakarta.persistence.*;
import lombok.Data;

@Data
public class GroceryProductDTO {
    private Long id;

    private String nameOfProduct;
    private String description;


    public GroceryProductDTO() { }

    public GroceryProductDTO(GroceryProduct groceryProduct){
        this.id = groceryProduct.getId();
        this.nameOfProduct = groceryProduct.getNameOfProduct();
        this.description = groceryProduct.getDescription();
    }

}