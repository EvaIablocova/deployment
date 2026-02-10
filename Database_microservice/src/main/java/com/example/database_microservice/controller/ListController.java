package com.example.database_microservice.controller;

import com.example.database_microservice.DTOs.AddFromMealPlanDTO;
import com.example.database_microservice.DTOs.ListDTO;
import com.example.database_microservice.model.ListType;
import com.example.database_microservice.service.ListService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/db/lists")
public class ListController {
    private final ListService listService;

    public ListController(ListService listService) {
        this.listService = listService;
    }

    @GetMapping
    public List<ListDTO> getAllLists() {
        return listService.getAllLists();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListDTO> getListById(@PathVariable Long id) {
        return listService.getListById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/group/{groupId}")
    public List<ListDTO> getListsByGroupId(@PathVariable Long groupId) {
        return listService.getListsByGroupId(groupId);
    }

    @GetMapping("/user/{userId}")
    public List<ListDTO> getListsByCreatedBy(@PathVariable Long userId) {
        return listService.getListsByCreatedBy(userId);
    }

    @GetMapping("/group/{groupId}/type/{listType}")
    public List<ListDTO> getListsByGroupIdAndType(@PathVariable Long groupId,
                                                   @PathVariable ListType listType) {
        return listService.getListsByGroupIdAndType(groupId, listType);
    }

    @PostMapping
    public ListDTO createList(@RequestBody ListDTO listDTO) {
        return listService.createList(listDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ListDTO> updateList(@PathVariable Long id, @RequestBody ListDTO updatedListDTO) {
        return listService.updateList(id, updatedListDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteList(@PathVariable Long id) {
        return listService.deleteList(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @PostMapping("/add-from-mealplan")
    public ResponseEntity<ListDTO> addIngredientsFromMealPlan(@RequestBody AddFromMealPlanDTO request) {
        try {
            ListDTO result = listService.addIngredientsFromMealPlan(request);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

}