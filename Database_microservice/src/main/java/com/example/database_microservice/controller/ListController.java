package com.example.database_microservice.controller;

import com.example.database_microservice.DTOs.ListDTO;
import com.example.database_microservice.DTOs.ListDTO;
import com.example.database_microservice.service.ListService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/db/lists")
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

}