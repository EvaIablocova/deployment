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
        List<ListItemDTO> listItems = listItemService.getAllListItems();
        return listItemService.getAllListItems();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListItemDTO> getListItemById(@PathVariable Long id) {
        return listItemService.getListItemById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ListItemDTO createListItem(@RequestBody ListItemDTO ListItemDTO) {
        return listItemService.createListItem(ListItemDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ListItemDTO> updateListItem(@PathVariable Long id, @RequestBody ListItemDTO updatedListItemDTO) {
        return listItemService.updateListItem(id, updatedListItemDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteListItem(@PathVariable Long id) {
        return listItemService.deleteListItem(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

}
