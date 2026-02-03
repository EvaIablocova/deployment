package com.example.database_microservice.model;

import com.example.database_microservice.DTOs.GroceryProductDTO;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "grocery_product")
public class GroceryProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nameOfProduct;

    @Column(columnDefinition = "TEXT")
    private String description;

    public GroceryProduct(){}

    public GroceryProduct(GroceryProductDTO groceryProductDTO){
        this.id = groceryProductDTO.getId();
        this.nameOfProduct = groceryProductDTO.getNameOfProduct();
        this.description = groceryProductDTO.getDescription();
    }

}