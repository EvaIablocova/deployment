package com.example.database_microservice.service;

import com.example.database_microservice.DTOs.ListDTO;
import com.example.database_microservice.model.Lists;
import com.example.database_microservice.repository.ListRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ListService {
    private final ListRepository listRepository;

    public ListService(ListRepository listRepository) {
        this.listRepository = listRepository;
    }

    // --- Mapping ---
    private ListDTO toDTO(Lists list) {
        return new ListDTO(list);
    }

    // --- CRUD ---
    public List<ListDTO> getAllLists() {
//        var lists = ListRepository.findAll();
        return listRepository.findAll().stream().map(this::toDTO).toList();
//        return new ArrayList<>();
    }

    public Optional<ListDTO> getListById(Long id) {
        return listRepository.findById(id).map(this::toDTO);
    }

    public ListDTO createList(ListDTO listDTO) {
        Lists list = new Lists(listDTO);
        return toDTO(listRepository.save(list));
    }

    public Optional<ListDTO> updateList(Long id, ListDTO updatedListDTO) {
        Lists updatedList = new Lists(updatedListDTO);
        return listRepository.findById(id).map(list -> {
            list.setTitle(updatedList.getTitle());
            list.setDescription(updatedList.getDescription());
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

}
