package com.example.database_microservice.controller;

import com.example.database_microservice.DTOs.ListItemDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.database_microservice.service.ListItemService;

import java.util.List;

@RestController
@RequestMapping("/db/listItems")
public class ListItemController {
    private final ListItemService listItemService;

    public ListItemController(ListItemService listItemService) {
        this.listItemService = listItemService;
    }

    @GetMapping
    public List<ListItemDTO> getAllListItems() {
        return listItemService.getAllListItems();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListItemDTO> getListItemById(@PathVariable Long id) {
        return listItemService.getListItemById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/list/{listId}")
    public List<ListItemDTO> getItemsByListId(@PathVariable Long listId) {
        return listItemService.getItemsByListId(listId);
    }

    @GetMapping("/list/{listId}/pending")
    public List<ListItemDTO> getPendingItemsByListId(@PathVariable Long listId) {
        return listItemService.getItemsByListIdAndDone(listId, false);
    }

    @GetMapping("/list/{listId}/done")
    public List<ListItemDTO> getDoneItemsByListId(@PathVariable Long listId) {
        return listItemService.getItemsByListIdAndDone(listId, true);
    }

    @PostMapping
    public ListItemDTO createListItem(@RequestBody ListItemDTO listItemDTO) {
        return listItemService.createListItem(listItemDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ListItemDTO> updateListItem(@PathVariable Long id, @RequestBody ListItemDTO updatedListItemDTO) {
        return listItemService.updateListItem(id, updatedListItemDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<Void> toggleDone(@PathVariable Long id) {
        return listItemService.toggleDone(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteListItem(@PathVariable Long id) {
        return listItemService.deleteListItem(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/list/{listId}")
    public ResponseEntity<Void> deleteItemsByListId(@PathVariable Long listId) {
        listItemService.deleteItemsByListId(listId);
        return ResponseEntity.noContent().build();
    }

}
