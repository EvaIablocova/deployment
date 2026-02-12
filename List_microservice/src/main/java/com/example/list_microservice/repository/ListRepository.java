package com.example.list_microservice.repository;

import com.example.list_microservice.DTOs.AddFromMealPlanDTO;
import com.example.list_microservice.DTOs.ListDTO;
import com.example.list_microservice.DTOs.ListItemDTO;
import com.example.list_microservice.DTOs.ListType;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public class ListRepository {

    private final RestTemplate restTemplate;
    private final String listsBase = "http://dbmicroservice:9009/db/lists";
    private final String itemsBase = "http://dbmicroservice:9009/db/listItems";

    public ListRepository(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    // ==================== Lists ====================

    public List<ListDTO> getAllLists() {
        try {
            ResponseEntity<ListDTO[]> response = restTemplate.getForEntity(listsBase, ListDTO[].class);
            ListDTO[] body = response.getBody();
            return Arrays.asList(Optional.ofNullable(body).orElse(new ListDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    public ResponseEntity<ListDTO> getListById(Long id) {
        try {
            ResponseEntity<ListDTO> response =
                    restTemplate.getForEntity(listsBase + "/" + id, ListDTO.class);
            return ResponseEntity.ok(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public List<ListDTO> getListsByGroupId(Long groupId) {
        try {
            ResponseEntity<ListDTO[]> response =
                    restTemplate.getForEntity(listsBase + "/group/" + groupId, ListDTO[].class);
            ListDTO[] body = response.getBody();
            return Arrays.asList(Optional.ofNullable(body).orElse(new ListDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    public List<ListDTO> getListsByUserId(Long userId) {
        try {
            ResponseEntity<ListDTO[]> response =
                    restTemplate.getForEntity(listsBase + "/user/" + userId, ListDTO[].class);
            ListDTO[] body = response.getBody();
            return Arrays.asList(Optional.ofNullable(body).orElse(new ListDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    public List<ListDTO> getListsByGroupIdAndType(Long groupId, ListType listType) {
        try {
            ResponseEntity<ListDTO[]> response =
                    restTemplate.getForEntity(listsBase + "/group/" + groupId + "/type/" + listType, ListDTO[].class);
            ListDTO[] body = response.getBody();
            return Arrays.asList(Optional.ofNullable(body).orElse(new ListDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    public ResponseEntity<ListDTO> createList(ListDTO listDTO) {
        try {
            ResponseEntity<ListDTO> response =
                    restTemplate.postForEntity(listsBase, listDTO, ListDTO.class);
            return ResponseEntity.status(HttpStatus.CREATED).body(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    public ResponseEntity<ListDTO> updateList(Long id, ListDTO updatedListDTO) {
        try {
            restTemplate.put(listsBase + "/" + id, updatedListDTO);
            return getListById(id);
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Void> deleteList(Long id) {
        try {
            restTemplate.delete(listsBase + "/" + id);
            return ResponseEntity.noContent().build();
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<ListDTO> addIngredientsFromMealPlan(AddFromMealPlanDTO request) {
        try {
            ResponseEntity<ListDTO> response =
                    restTemplate.postForEntity(listsBase + "/add-from-mealplan", request, ListDTO.class);
            return ResponseEntity.ok(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // ==================== List Items ====================

    public List<ListItemDTO> getAllListItems() {
        try {
            ResponseEntity<ListItemDTO[]> response = restTemplate.getForEntity(itemsBase, ListItemDTO[].class);
            ListItemDTO[] body = response.getBody();
            return Arrays.asList(Optional.ofNullable(body).orElse(new ListItemDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    public ResponseEntity<ListItemDTO> getListItemById(Long id) {
        try {
            ResponseEntity<ListItemDTO> response =
                    restTemplate.getForEntity(itemsBase + "/" + id, ListItemDTO.class);
            return ResponseEntity.ok(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public List<ListItemDTO> getItemsByListId(Long listId) {
        try {
            ResponseEntity<ListItemDTO[]> response =
                    restTemplate.getForEntity(itemsBase + "/list/" + listId, ListItemDTO[].class);
            ListItemDTO[] body = response.getBody();
            return Arrays.asList(Optional.ofNullable(body).orElse(new ListItemDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    public List<ListItemDTO> getPendingItemsByListId(Long listId) {
        try {
            ResponseEntity<ListItemDTO[]> response =
                    restTemplate.getForEntity(itemsBase + "/list/" + listId + "/pending", ListItemDTO[].class);
            ListItemDTO[] body = response.getBody();
            return Arrays.asList(Optional.ofNullable(body).orElse(new ListItemDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    public List<ListItemDTO> getDoneItemsByListId(Long listId) {
        try {
            ResponseEntity<ListItemDTO[]> response =
                    restTemplate.getForEntity(itemsBase + "/list/" + listId + "/done", ListItemDTO[].class);
            ListItemDTO[] body = response.getBody();
            return Arrays.asList(Optional.ofNullable(body).orElse(new ListItemDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    public ResponseEntity<ListItemDTO> createListItem(ListItemDTO itemDTO) {
        try {
            ResponseEntity<ListItemDTO> response =
                    restTemplate.postForEntity(itemsBase, itemDTO, ListItemDTO.class);
            return ResponseEntity.status(HttpStatus.CREATED).body(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    public ResponseEntity<ListItemDTO> updateListItem(Long id, ListItemDTO updatedItemDTO) {
        try {
            restTemplate.put(itemsBase + "/" + id, updatedItemDTO);
            return getListItemById(id);
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Void> toggleItemDone(Long id) {
        try {
            restTemplate.patchForObject(itemsBase + "/" + id + "/toggle", null, Void.class);
            return ResponseEntity.ok().build();
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Void> deleteListItem(Long id) {
        try {
            restTemplate.delete(itemsBase + "/" + id);
            return ResponseEntity.noContent().build();
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Void> deleteItemsByListId(Long listId) {
        try {
            restTemplate.delete(itemsBase + "/list/" + listId);
            return ResponseEntity.noContent().build();
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ==================== Path-based List Item endpoints ====================

    public ResponseEntity<ListItemDTO> createListItemForList(Long listId, ListItemDTO itemDTO) {
        try {
            ResponseEntity<ListItemDTO> response =
                    restTemplate.postForEntity(itemsBase + "/list/" + listId, itemDTO, ListItemDTO.class);
            return ResponseEntity.status(HttpStatus.CREATED).body(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    public ResponseEntity<ListItemDTO> updateListItemInList(Long listId, Long itemId, ListItemDTO updatedItemDTO) {
        try {
            restTemplate.put(itemsBase + "/list/" + listId + "/item/" + itemId, updatedItemDTO);
            return getListItemById(itemId);
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Void> toggleItemDoneInList(Long listId, Long itemId) {
        try {
            restTemplate.patchForObject(itemsBase + "/list/" + listId + "/item/" + itemId + "/toggle", null, Void.class);
            return ResponseEntity.ok().build();
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Void> deleteListItemFromList(Long listId, Long itemId) {
        try {
            restTemplate.delete(itemsBase + "/list/" + listId + "/item/" + itemId);
            return ResponseEntity.noContent().build();
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

}

