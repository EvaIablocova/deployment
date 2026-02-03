package com.example.database_microservice.controller;

import com.example.database_microservice.DTOs.GroceryProductDTO;
import com.example.database_microservice.service.GroceryProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/db/groceryProduct")
public class GroceryProductController {
    private final GroceryProductService groceryProductService;

    public GroceryProductController(GroceryProductService groceryProductService) {
        this.groceryProductService = groceryProductService;
    }

    @GetMapping
    public List<GroceryProductDTO> getAllGroceryProducts() {
        List<GroceryProductDTO> groceryProducts = groceryProductService.getAllGroceryProducts();
        return groceryProductService.getAllGroceryProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroceryProductDTO> getGroceryProductById(@PathVariable Long id) {
        return groceryProductService.getGroceryProductById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public GroceryProductDTO createGroceryProduct(@RequestBody GroceryProductDTO groceryProductDTO) {
        return groceryProductService.createGroceryProduct(groceryProductDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroceryProductDTO> updateGroceryProduct(@PathVariable Long id, @RequestBody GroceryProductDTO updatedGroceryProductDTO) {
        return groceryProductService.updateGroceryProduct(id, updatedGroceryProductDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroceryProduct(@PathVariable Long id) {
        return groceryProductService.deleteGroceryProduct(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

}