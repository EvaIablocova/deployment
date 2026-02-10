package com.example.database_microservice.service;

import com.example.database_microservice.DTOs.AddFromMealPlanDTO;
import com.example.database_microservice.DTOs.ListDTO;
import com.example.database_microservice.DTOs.ListItemDTO;
import com.example.database_microservice.model.*;
import com.example.database_microservice.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ListService {
    private final ListRepository listRepository;
    private final ListItemRepository listItemRepository;
    private final MealPlanRepository mealPlanRepository;
    private final ProductRepository productRepository;

    public ListService(ListRepository listRepository,
                       ListItemRepository listItemRepository,
                       MealPlanRepository mealPlanRepository,
                       ProductRepository productRepository) {
        this.listRepository = listRepository;
        this.listItemRepository = listItemRepository;
        this.mealPlanRepository = mealPlanRepository;
        this.productRepository = productRepository;
    }

    // --- Mapping ---
    private ListDTO toDTO(Lists list) {
        return new ListDTO(list);
    }

    // --- CRUD ---
    public List<ListDTO> getAllLists() {
        return listRepository.findAll().stream().map(this::toDTO).toList();
    }

    public Optional<ListDTO> getListById(Long id) {
        return listRepository.findById(id).map(this::toDTO);
    }

    public List<ListDTO> getListsByGroupId(Long groupId) {
        return listRepository.findByGroupIdOrderByUpdatedAtDesc(groupId).stream()
                .map(this::toDTO).toList();
    }

    public List<ListDTO> getListsByCreatedBy(Long userId) {
        return listRepository.findByCreatedBy(userId).stream()
                .map(this::toDTO).toList();
    }

    public List<ListDTO> getListsByGroupIdAndType(Long groupId, ListType listType) {
        return listRepository.findByGroupIdAndListType(groupId, listType).stream()
                .map(this::toDTO).toList();
    }

    public ListDTO createList(ListDTO listDTO) {
        Lists list = new Lists(listDTO);
        return toDTO(listRepository.save(list));
    }

    public Optional<ListDTO> updateList(Long id, ListDTO updatedListDTO) {
        return listRepository.findById(id).map(list -> {
            if (updatedListDTO.getTitle() != null) {
                list.setTitle(updatedListDTO.getTitle());
            }
            if (updatedListDTO.getDescription() != null) {
                list.setDescription(updatedListDTO.getDescription());
            }
            if (updatedListDTO.getGroupId() != null) {
                list.setGroupId(updatedListDTO.getGroupId());
            }
            if (updatedListDTO.getListType() != null) {
                list.setListType(updatedListDTO.getListType());
            }
            return toDTO(listRepository.save(list));
        });
    }

    public boolean deleteList(Long id) {
        if (listRepository.existsById(id)) {
            listRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // --- Add ingredients from Meal Plan ---
    @Transactional
    public ListDTO addIngredientsFromMealPlan(AddFromMealPlanDTO request) {
        // Get or create the target list
        Lists targetList;
        if (request.getListId() != null) {
            targetList = listRepository.findById(request.getListId())
                    .orElseThrow(() -> new RuntimeException("List not found: " + request.getListId()));
        } else {
            // Create new list
            targetList = new Lists();
            targetList.setTitle(request.getNewListTitle() != null ?
                    request.getNewListTitle() : "Shopping List");
            targetList.setGroupId(request.getGroupId());
            targetList.setCreatedBy(request.getCreatedBy());
            targetList.setListType(ListType.SHOPPING);
            targetList = listRepository.save(targetList);
        }

        // Get meal plan with all days and items
        MealPlan mealPlan = mealPlanRepository.findById(request.getMealPlanId())
                .orElseThrow(() -> new RuntimeException("MealPlan not found: " + request.getMealPlanId()));

        // Get current max sort order
        Integer maxSortOrder = listItemRepository.findMaxSortOrderByListId(targetList.getId());
        int sortOrder = maxSortOrder != null ? maxSortOrder + 1 : 0;

        List<ListItem> newItems = new ArrayList<>();

        // Iterate through all days and items
        for (MealPlanDay day : mealPlan.getDays()) {
            for (MealPlanItem mealItem : day.getItems()) {
                // Check if this meal item has a recipe
                if (mealItem.getRecipe() != null) {
                    Recipe recipe = mealItem.getRecipe();

                    // Skip if specific recipes are requested and this one is not in the list
                    if (request.getRecipeIds() != null && !request.getRecipeIds().isEmpty()
                            && !request.getRecipeIds().contains(recipe.getId())) {
                        continue;
                    }

                    // Add all ingredients from this recipe
                    for (RecipeIngredient ingredient : recipe.getIngredients()) {
                        ListItem item = new ListItem();
                        item.setList(targetList);
                        item.setSourceMealPlanId(mealPlan.getId());
                        item.setSourceRecipeId(recipe.getId());
                        item.setSortOrder(sortOrder++);
                        item.setDone(false);

                        if (ingredient.getProduct() != null) {
                            // Product from catalog
                            item.setProduct(ingredient.getProduct());
                            item.setItemType(ListItemType.RECIPE_INGREDIENT);
                            item.setTextOfItem(ingredient.getProduct().getNameOfProduct());
                        } else if (ingredient.getCustomIngredientName() != null) {
                            // Custom ingredient
                            item.setItemType(ListItemType.RECIPE_INGREDIENT);
                            item.setTextOfItem(ingredient.getCustomIngredientName());
                        }

                        item.setQuantity(ingredient.getQuantity());
                        item.setUnit(ingredient.getUnit());

                        newItems.add(item);
                    }
                }

                // Also add standalone products from meal items
                if (mealItem.getProduct() != null) {
                    ListItem item = new ListItem();
                    item.setList(targetList);
                    item.setProduct(mealItem.getProduct());
                    item.setTextOfItem(mealItem.getProduct().getNameOfProduct());
                    item.setItemType(ListItemType.PRODUCT);
                    item.setSourceMealPlanId(mealPlan.getId());
                    item.setSortOrder(sortOrder++);
                    item.setDone(false);
                    item.setQuantity(1.0);

                    newItems.add(item);
                }
            }
        }

        // Save all new items
        listItemRepository.saveAll(newItems);

        // Refresh and return
        return toDTO(listRepository.findById(targetList.getId()).orElse(targetList));
    }

}
