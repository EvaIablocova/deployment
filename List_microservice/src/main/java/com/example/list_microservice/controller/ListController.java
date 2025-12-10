package com.example.list_microservice.controller;

import com.example.list_microservice.DTOs.ListDTO;
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

    @GetMapping
    public List<ListDTO> getAllLists() {
        List<ListDTO> lists = listService.getAllLists();
        return listService.getAllLists();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListDTO> getListById(@PathVariable Long id) {
        return listService.getListById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ListDTO createList(@RequestBody ListDTO ListDTO) {
        return listService.createList(ListDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ListDTO> updateList(@PathVariable Long id, @RequestBody ListDTO updatedListDTO) {
        return listService.updateList(id, updatedListDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteList(@PathVariable Long id) {
        return listService.deleteList(id);
    }


}
