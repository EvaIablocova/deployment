package com.example.database_microservice.service;

import com.example.database_microservice.DTOs.ListItemDTO;
import com.example.database_microservice.model.ListItem;
import com.example.database_microservice.repository.ListItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ListItemService {
    private final ListItemRepository listItemRepository;

    public ListItemService(ListItemRepository listItemRepository) {
        this.listItemRepository = listItemRepository;
    }

    // --- Mapping ---
    private ListItemDTO toDTO(ListItem listItem) {
        return new ListItemDTO(listItem);
    }

    // --- CRUD ---
    public List<ListItemDTO> getAllListItems() {
//        var listItems = listItemRepository.findAll();
        return listItemRepository.findAll().stream().map(this::toDTO).toList();
//        return new ArrayList<>();
    }

    public Optional<ListItemDTO> getListItemById(Long id) {
        return listItemRepository.findById(id).map(this::toDTO);
    }

    public ListItemDTO createListItem(ListItemDTO listItemDTO) {
        ListItem listItem = new ListItem(listItemDTO);
        return toDTO(listItemRepository.save(listItem));
    }

    public Optional<ListItemDTO> updateListItem(Long id, ListItemDTO updatedListItemDTO) {
        ListItem updatedListItem = new ListItem(updatedListItemDTO);
        return listItemRepository.findById(id).map(listItem -> {
            listItem.setTextOfItem(updatedListItem.getTextOfItem());
            listItem.setDone(updatedListItem.isDone());
            return toDTO(listItemRepository.save(listItem));
        });
    }

    public boolean deleteListItem(Long id) {
        if (listItemRepository.existsById(id)) {
            listItemRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
