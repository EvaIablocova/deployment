package com.example.list_microservice.service;

import com.example.list_microservice.DTOs.*;
import com.example.list_microservice.repository.ListRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ListService {

    private final ListRepository listRepository;

    public ListService(ListRepository listRepository) {
        this.listRepository = listRepository;
    }

    // ==================== Lists ====================

    public List<ListDTO> getAllLists() {
        return listRepository.getAllLists();
    }

    public Optional<ListDTO> getListById(Long id) {
        return Optional.ofNullable(listRepository.getListById(id))
                .map(ResponseEntity::getBody);
    }

    public List<ListDTO> getListsByGroupId(Long groupId) {
        return listRepository.getListsByGroupId(groupId);
    }

    public List<ListDTO> getListsByUserId(Long userId) {
        return listRepository.getListsByUserId(userId);
    }

    public List<ListDTO> getListsByGroupIdAndType(Long groupId, ListType listType) {
        return listRepository.getListsByGroupIdAndType(groupId, listType);
    }

    public ListDTO createList(ListDTO listDTO) {
        return listRepository.createList(listDTO).getBody();
    }

    public ResponseEntity<ListDTO> updateList(Long id, ListDTO updatedListDTO) {
        return listRepository.updateList(id, updatedListDTO);
    }

    public ResponseEntity<Void> deleteList(Long id) {
        return listRepository.deleteList(id);
    }

    public ResponseEntity<ListDTO> addIngredientsFromMealPlan(AddFromMealPlanDTO request) {
        return listRepository.addIngredientsFromMealPlan(request);
    }

    // ==================== List Items ====================

    public List<ListItemDTO> getAllListItems() {
        return listRepository.getAllListItems();
    }

    public Optional<ListItemDTO> getListItemById(Long id) {
        return Optional.ofNullable(listRepository.getListItemById(id))
                .map(ResponseEntity::getBody);
    }

    public List<ListItemDTO> getItemsByListId(Long listId) {
        return listRepository.getItemsByListId(listId);
    }

    public List<ListItemDTO> getPendingItemsByListId(Long listId) {
        return listRepository.getPendingItemsByListId(listId);
    }

    public List<ListItemDTO> getDoneItemsByListId(Long listId) {
        return listRepository.getDoneItemsByListId(listId);
    }

    public ListItemDTO createListItem(ListItemDTO itemDTO) {
        return listRepository.createListItem(itemDTO).getBody();
    }

    public ResponseEntity<ListItemDTO> updateListItem(Long id, ListItemDTO updatedItemDTO) {
        return listRepository.updateListItem(id, updatedItemDTO);
    }

    public ResponseEntity<Void> toggleItemDone(Long id) {
        return listRepository.toggleItemDone(id);
    }

    public ResponseEntity<Void> deleteListItem(Long id) {
        return listRepository.deleteListItem(id);
    }

    public ResponseEntity<Void> deleteItemsByListId(Long listId) {
        return listRepository.deleteItemsByListId(listId);
    }

}
