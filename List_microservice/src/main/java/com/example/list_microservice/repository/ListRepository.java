package com.example.list_microservice.repository;

import com.example.list_microservice.DTOs.ListDTO;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public class ListRepository {

    private final RestTemplate restTemplate;
    private final String externalBase = "http://dbmicroservice:9009/db/lists";

    public ListRepository(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }


    public List<ListDTO> getAllLists() {
        try {
            ResponseEntity<ListDTO[]> response = restTemplate.getForEntity(externalBase, ListDTO[].class);
            ListDTO[] body = response.getBody();
            return Arrays.asList(Optional.ofNullable(body).orElse(new ListDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }


    public ResponseEntity<ListDTO> getListById(@PathVariable Long id) {
        try {
            ResponseEntity<ListDTO> response =
                    restTemplate.getForEntity(externalBase + "/" + id, ListDTO.class);
            return ResponseEntity.ok(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }


    public ResponseEntity<ListDTO> createList(@RequestBody ListDTO listDTO) {
        try {
            ResponseEntity<ListDTO> response =
                    restTemplate.postForEntity(externalBase, listDTO, ListDTO.class);
            return ResponseEntity.status(HttpStatus.CREATED).body(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    public ResponseEntity<ListDTO> updateList(@PathVariable Long id, @RequestBody ListDTO updatedListDTO) {
        try {
            restTemplate.put(externalBase + "/" + id, updatedListDTO);
            return ResponseEntity.ok(updatedListDTO);
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Void> deleteList(@PathVariable Long id) {
        try {
            restTemplate.delete(externalBase + "/" + id);
            return ResponseEntity.noContent().build();
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }


}

