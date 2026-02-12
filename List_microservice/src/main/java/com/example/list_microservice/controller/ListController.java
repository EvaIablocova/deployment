package com.example.list_microservice.controller;

import com.example.list_microservice.DTOs.*;
import com.example.list_microservice.service.ListService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lists")
public class ListController {

    private final ListService listService;

    public ListController(ListService listService) {
        this.listService = listService;
    }

    // ==================== Lists ====================

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
    public List<ListDTO> getListsByUserId(@PathVariable Long userId) {
        return listService.getListsByUserId(userId);
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
        return listService.updateList(id, updatedListDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteList(@PathVariable Long id) {
        return listService.deleteList(id);
    }

    @PostMapping("/add-from-mealplan")
    public ResponseEntity<ListDTO> addIngredientsFromMealPlan(@RequestBody AddFromMealPlanDTO request) {
        return listService.addIngredientsFromMealPlan(request);
    }

    // ==================== List Items ====================

    @GetMapping("/items")
    public List<ListItemDTO> getAllListItems() {
        return listService.getAllListItems();
    }

    @GetMapping("/items/{id}")
    public ResponseEntity<ListItemDTO> getListItemById(@PathVariable Long id) {
        return listService.getListItemById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{listId}/items")
    public List<ListItemDTO> getItemsByListId(@PathVariable Long listId) {
        return listService.getItemsByListId(listId);
    }

    @GetMapping("/{listId}/items/pending")
    public List<ListItemDTO> getPendingItemsByListId(@PathVariable Long listId) {
        return listService.getPendingItemsByListId(listId);
    }

    @GetMapping("/{listId}/items/done")
    public List<ListItemDTO> getDoneItemsByListId(@PathVariable Long listId) {
        return listService.getDoneItemsByListId(listId);
    }

    @PostMapping("/items")
    public ListItemDTO createListItem(@RequestBody ListItemDTO itemDTO) {
        return listService.createListItem(itemDTO);
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<ListItemDTO> updateListItem(@PathVariable Long id, @RequestBody ListItemDTO updatedItemDTO) {
        return listService.updateListItem(id, updatedItemDTO);
    }

    @PatchMapping("/items/{id}/toggle")
    public ResponseEntity<Void> toggleItemDone(@PathVariable Long id) {
        return listService.toggleItemDone(id);
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> deleteListItem(@PathVariable Long id) {
        return listService.deleteListItem(id);
    }

    @DeleteMapping("/{listId}/items")
    public ResponseEntity<Void> deleteItemsByListId(@PathVariable Long listId) {
        return listService.deleteItemsByListId(listId);
    }

    // ==================== Path-based List Item endpoints ====================

    @PostMapping("/{listId}/items")
    public ResponseEntity<ListItemDTO> createListItemForList(@PathVariable Long listId,
                                                              @RequestBody ListItemDTO itemDTO) {
        return listService.createListItemForList(listId, itemDTO);
    }

    @PutMapping("/{listId}/items/{itemId}")
    public ResponseEntity<ListItemDTO> updateListItemInList(@PathVariable Long listId,
                                                             @PathVariable Long itemId,
                                                             @RequestBody ListItemDTO updatedItemDTO) {
        return listService.updateListItemInList(listId, itemId, updatedItemDTO);
    }

    @PatchMapping("/{listId}/items/{itemId}/toggle")
    public ResponseEntity<Void> toggleItemDoneInList(@PathVariable Long listId, @PathVariable Long itemId) {
        return listService.toggleItemDoneInList(listId, itemId);
    }

    @DeleteMapping("/{listId}/items/{itemId}")
    public ResponseEntity<Void> deleteListItemFromList(@PathVariable Long listId, @PathVariable Long itemId) {
        return listService.deleteListItemFromList(listId, itemId);
    }

}
