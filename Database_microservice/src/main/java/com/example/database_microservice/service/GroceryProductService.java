package com.example.database_microservice.service;

import com.example.database_microservice.DTOs.GroceryProductDTO;
import com.example.database_microservice.model.GroceryProduct;
import com.example.database_microservice.repository.GroceryProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroceryProductService {
    private final GroceryProductRepository groceryProductRepository;

    public GroceryProductService(GroceryProductRepository groceryProductRepository) {
        this.groceryProductRepository = groceryProductRepository;
    }

    // --- Mapping ---
    private GroceryProductDTO toDTO(GroceryProduct groceryProduct) {
        return new GroceryProductDTO(groceryProduct);
    }

    // --- CRUD ---
    public List<GroceryProductDTO> getAllGroceryProducts() {
//        var groceryProducts = groceryProductRepository.findAll();
        return groceryProductRepository.findAll().stream().map(this::toDTO).toList();
//        return new ArrayList<>();
    }

    public Optional<GroceryProductDTO> getGroceryProductById(Long id) {
        return groceryProductRepository.findById(id).map(this::toDTO);
    }

    public GroceryProductDTO createGroceryProduct(GroceryProductDTO groceryProductDTO) {
        GroceryProduct groceryProduct = new GroceryProduct(groceryProductDTO);
        return toDTO(groceryProductRepository.save(groceryProduct));
    }

    public Optional<GroceryProductDTO> updateGroceryProduct(Long id, GroceryProductDTO updatedGroceryProductDTO) {
        GroceryProduct updatedGroceryProduct = new GroceryProduct(updatedGroceryProductDTO);
        return groceryProductRepository.findById(id).map(groceryProduct -> {
            groceryProduct.setNameOfProduct(updatedGroceryProduct.getNameOfProduct());
            groceryProduct.setDescription(updatedGroceryProduct.getDescription());
            return toDTO(groceryProductRepository.save(groceryProduct));
        });
    }

    public boolean deleteGroceryProduct(Long id) {
        if (groceryProductRepository.existsById(id)) {
            groceryProductRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
