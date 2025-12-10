package com.example.list_microservice.service;

import com.example.list_microservice.DTOs.ListDTO;
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

    public List<ListDTO> getAllLists() {
        return listRepository.getAllLists();
    }

   public Optional<ListDTO> getListById(Long id) {
       return Optional.ofNullable(listRepository.getListById(id))
                      .map(response -> response.getBody());
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


}
