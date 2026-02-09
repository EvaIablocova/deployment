package com.example.database_microservice.repository;

import com.example.database_microservice.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findByGroupId(Long groupId);
    List<Recipe> findByCreatedBy(Long userId);
}
