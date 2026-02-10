package com.example.database_microservice.service;

import com.example.database_microservice.DTOs.ListItemDTO;
import com.example.database_microservice.model.ListItem;
import com.example.database_microservice.model.ListItemType;
import com.example.database_microservice.model.Lists;
import com.example.database_microservice.model.Product;
import com.example.database_microservice.repository.ListItemRepository;
import com.example.database_microservice.repository.ListRepository;
import com.example.database_microservice.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ListItemService {
    private final ListItemRepository listItemRepository;
    private final ListRepository listRepository;
    private final ProductRepository productRepository;

    public ListItemService(ListItemRepository listItemRepository,
                           ListRepository listRepository,
                           ProductRepository productRepository) {
        this.listItemRepository = listItemRepository;
        this.listRepository = listRepository;
        this.productRepository = productRepository;
    }

    // --- Mapping ---
    private ListItemDTO toDTO(ListItem listItem) {
        return new ListItemDTO(listItem);
    }

    // --- CRUD ---
    public List<ListItemDTO> getAllListItems() {
        return listItemRepository.findAll().stream().map(this::toDTO).toList();
    }

    public Optional<ListItemDTO> getListItemById(Long id) {
        return listItemRepository.findById(id).map(this::toDTO);
    }

    public List<ListItemDTO> getItemsByListId(Long listId) {
        return listItemRepository.findByListIdOrderBySortOrderAsc(listId).stream()
                .map(this::toDTO).toList();
    }

    public List<ListItemDTO> getItemsByListIdAndDone(Long listId, boolean done) {
        return listItemRepository.findByListIdAndDone(listId, done).stream()
                .map(this::toDTO).toList();
    }

    @Transactional
    public ListItemDTO createListItem(ListItemDTO dto) {
        Lists list = listRepository.findById(dto.getListId())
                .orElseThrow(() -> new RuntimeException("List not found: " + dto.getListId()));

        ListItem item = new ListItem();
        item.setList(list);
        item.setTextOfItem(dto.getTextOfItem());
        item.setQuantity(dto.getQuantity());
        item.setUnit(dto.getUnit());
        item.setSourceMealPlanId(dto.getSourceMealPlanId());
        item.setSourceRecipeId(dto.getSourceRecipeId());
        item.setItemType(dto.getItemType() != null ? dto.getItemType() : ListItemType.CUSTOM);
        item.setDone(dto.isDone());

        // Set product if productId provided
        if (dto.getProductId() != null) {
            Product product = productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + dto.getProductId()));
            item.setProduct(product);
            if (item.getItemType() == ListItemType.CUSTOM) {
                item.setItemType(ListItemType.PRODUCT);
            }
        }

        // Set sort order
        if (dto.getSortOrder() != null) {
            item.setSortOrder(dto.getSortOrder());
        } else {
            Integer maxOrder = listItemRepository.findMaxSortOrderByListId(list.getId());
            item.setSortOrder(maxOrder != null ? maxOrder + 1 : 0);
        }

        return toDTO(listItemRepository.save(item));
    }

    @Transactional
    public Optional<ListItemDTO> updateListItem(Long id, ListItemDTO dto) {
        return listItemRepository.findById(id).map(item -> {
            if (dto.getTextOfItem() != null) {
                item.setTextOfItem(dto.getTextOfItem());
            }
            if (dto.getQuantity() != null) {
                item.setQuantity(dto.getQuantity());
            }
            if (dto.getUnit() != null) {
                item.setUnit(dto.getUnit());
            }
            if (dto.getItemType() != null) {
                item.setItemType(dto.getItemType());
            }
            if (dto.getSortOrder() != null) {
                item.setSortOrder(dto.getSortOrder());
            }
            if (dto.getProductId() != null) {
                Product product = productRepository.findById(dto.getProductId())
                        .orElseThrow(() -> new RuntimeException("Product not found: " + dto.getProductId()));
                item.setProduct(product);
            }
            item.setDone(dto.isDone());
            return toDTO(listItemRepository.save(item));
        });
    }

    @Transactional
    public boolean toggleDone(Long id) {
        return listItemRepository.findById(id).map(item -> {
            item.setDone(!item.isDone());
            listItemRepository.save(item);
            return true;
        }).orElse(false);
    }

    public boolean deleteListItem(Long id) {
        if (listItemRepository.existsById(id)) {
            listItemRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional
    public void deleteItemsByListId(Long listId) {
        listItemRepository.deleteByListId(listId);
    }

}
