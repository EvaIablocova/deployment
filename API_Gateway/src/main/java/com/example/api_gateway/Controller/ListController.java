package com.example.api_gateway.Controller;

import com.example.api_gateway.DTOs.*;
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

    // ==================== Lists ====================

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

    @GetMapping("/group/{groupId}")
    public List<ListDTO> getListsByGroupId(@PathVariable Long groupId) {
        try {
            ResponseEntity<ListDTO[]> response =
                    restTemplate.getForEntity(externalBase + "/group/" + groupId, ListDTO[].class);
            ListDTO[] body = response.getBody();
            return Arrays.asList(Optional.ofNullable(body).orElse(new ListDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    @GetMapping("/user/{userId}")
    public List<ListDTO> getListsByUserId(@PathVariable Long userId) {
        try {
            ResponseEntity<ListDTO[]> response =
                    restTemplate.getForEntity(externalBase + "/user/" + userId, ListDTO[].class);
            ListDTO[] body = response.getBody();
            return Arrays.asList(Optional.ofNullable(body).orElse(new ListDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    @GetMapping("/group/{groupId}/type/{listType}")
    public List<ListDTO> getListsByGroupIdAndType(@PathVariable Long groupId,
                                                   @PathVariable ListType listType) {
        try {
            ResponseEntity<ListDTO[]> response =
                    restTemplate.getForEntity(externalBase + "/group/" + groupId + "/type/" + listType, ListDTO[].class);
            ListDTO[] body = response.getBody();
            return Arrays.asList(Optional.ofNullable(body).orElse(new ListDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    @PostMapping
    public ResponseEntity<ListDTO> createList(@RequestBody ListDTO listDTO) {
        try {
            ResponseEntity<ListDTO> response =
                    restTemplate.postForEntity(externalBase, listDTO, ListDTO.class);
            return ResponseEntity.status(HttpStatus.CREATED).body(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ListDTO> updateList(@PathVariable Long id, @RequestBody ListDTO updatedListDTO) {
        try {
            restTemplate.put(externalBase + "/" + id, updatedListDTO);
            return getListById(id);
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

    @PostMapping("/add-from-mealplan")
    public ResponseEntity<ListDTO> addIngredientsFromMealPlan(@RequestBody AddFromMealPlanDTO request) {
        try {
            ResponseEntity<ListDTO> response =
                    restTemplate.postForEntity(externalBase + "/add-from-mealplan", request, ListDTO.class);
            return ResponseEntity.ok(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // ==================== List Items ====================

    @GetMapping("/items")
    public List<ListItemDTO> getAllListItems() {
        try {
            ResponseEntity<ListItemDTO[]> response =
                    restTemplate.getForEntity(externalBase + "/items", ListItemDTO[].class);
            ListItemDTO[] body = response.getBody();
            return Arrays.asList(Optional.ofNullable(body).orElse(new ListItemDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    @GetMapping("/items/{id}")
    public ResponseEntity<ListItemDTO> getListItemById(@PathVariable Long id) {
        try {
            ResponseEntity<ListItemDTO> response =
                    restTemplate.getForEntity(externalBase + "/items/" + id, ListItemDTO.class);
            return ResponseEntity.ok(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{listId}/items")
    public List<ListItemDTO> getItemsByListId(@PathVariable Long listId) {
        try {
            ResponseEntity<ListItemDTO[]> response =
                    restTemplate.getForEntity(externalBase + "/" + listId + "/items", ListItemDTO[].class);
            ListItemDTO[] body = response.getBody();
            return Arrays.asList(Optional.ofNullable(body).orElse(new ListItemDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    @GetMapping("/{listId}/items/pending")
    public List<ListItemDTO> getPendingItemsByListId(@PathVariable Long listId) {
        try {
            ResponseEntity<ListItemDTO[]> response =
                    restTemplate.getForEntity(externalBase + "/" + listId + "/items/pending", ListItemDTO[].class);
            ListItemDTO[] body = response.getBody();
            return Arrays.asList(Optional.ofNullable(body).orElse(new ListItemDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    @GetMapping("/{listId}/items/done")
    public List<ListItemDTO> getDoneItemsByListId(@PathVariable Long listId) {
        try {
            ResponseEntity<ListItemDTO[]> response =
                    restTemplate.getForEntity(externalBase + "/" + listId + "/items/done", ListItemDTO[].class);
            ListItemDTO[] body = response.getBody();
            return Arrays.asList(Optional.ofNullable(body).orElse(new ListItemDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    @PostMapping("/items")
    public ResponseEntity<ListItemDTO> createListItem(@RequestBody ListItemDTO itemDTO) {
        try {
            ResponseEntity<ListItemDTO> response =
                    restTemplate.postForEntity(externalBase + "/items", itemDTO, ListItemDTO.class);
            return ResponseEntity.status(HttpStatus.CREATED).body(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<ListItemDTO> updateListItem(@PathVariable Long id, @RequestBody ListItemDTO updatedItemDTO) {
        try {
            restTemplate.put(externalBase + "/items/" + id, updatedItemDTO);
            return getListItemById(id);
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/items/{id}/toggle")
    public ResponseEntity<Void> toggleItemDone(@PathVariable Long id) {
        try {
            restTemplate.patchForObject(externalBase + "/items/" + id + "/toggle", null, Void.class);
            return ResponseEntity.ok().build();
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> deleteListItem(@PathVariable Long id) {
        try {
            restTemplate.delete(externalBase + "/items/" + id);
            return ResponseEntity.noContent().build();
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{listId}/items")
    public ResponseEntity<Void> deleteItemsByListId(@PathVariable Long listId) {
        try {
            restTemplate.delete(externalBase + "/" + listId + "/items");
            return ResponseEntity.noContent().build();
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
