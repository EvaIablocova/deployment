package com.example.database_microservice.repository;

import com.example.database_microservice.model.GroceryProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroceryProductRepository extends JpaRepository<GroceryProduct, Long> {

}


