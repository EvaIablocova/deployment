package com.example.api_gateway.Controller;

import com.example.api_gateway.DTOs.ListDTO;
import com.example.api_gateway.config.ServiceUrlsConfig;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api_gateway/lists")
public class ListController {

    private final RestTemplate restTemplate;
    private final String externalBase;

    public ListController(RestTemplateBuilder builder, ServiceUrlsConfig serviceUrls) {
        this.restTemplate = builder.build();
        this.externalBase = serviceUrls.getListServiceUrl() + "/api/lists";
    }

    // --- CRUD ---

    @GetMapping
    public List<ListDTO> getAllLists() {
        try {
            ResponseEntity<ListDTO[]> response = restTemplate.getForEntity(externalBase, ListDTO[].class);
            ListDTO[] body = response.getBody();
            return Arrays.asList(Optional.ofNullable(body).orElse(new ListDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListDTO> getListById(@PathVariable Long id) {
        try {
            ResponseEntity<ListDTO> response =
                    restTemplate.getForEntity(externalBase + "/" + id, ListDTO.class);
            return ResponseEntity.ok(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<ListDTO> createList(@RequestBody ListDTO ListDTO) {
        try {
            ResponseEntity<ListDTO> response =
                    restTemplate.postForEntity(externalBase, ListDTO, ListDTO.class);
            return ResponseEntity.status(HttpStatus.CREATED).body(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ListDTO> updateList(@PathVariable Long id, @RequestBody ListDTO updatedListDTO) {
        try {
            restTemplate.put(externalBase + "/" + id, updatedListDTO);
            return ResponseEntity.ok(updatedListDTO);
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteList(@PathVariable Long id) {
        try {
            restTemplate.delete(externalBase + "/" + id);
            return ResponseEntity.noContent().build();
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }


}
