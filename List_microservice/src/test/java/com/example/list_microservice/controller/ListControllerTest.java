package com.example.list_microservice.controller;

import com.example.list_microservice.DTOs.ListDTO;
import com.example.list_microservice.service.ListService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListControllerTest {

    @Mock
    private ListService listService;

    @InjectMocks
    private ListController listController;

    @Test
    void getAllLists_returnsList() {
        ListDTO dto1 = mock(ListDTO.class);
        ListDTO dto2 = mock(ListDTO.class);
        List<ListDTO> expected = Arrays.asList(dto1, dto2);

        when(listService.getAllLists()).thenReturn(expected);

        List<ListDTO> result = listController.getAllLists();

        assertNotNull(result);
        assertEquals(expected, result);
        verify(listService, times(1)).getAllLists();
    }

    @Test
    void getListById_found_returnsOkWithBody() {
        Long id = 1L;
        ListDTO dto = mock(ListDTO.class);

        when(listService.getListById(id)).thenReturn(Optional.of(dto));

        ResponseEntity<ListDTO> response = listController.getListById(id);

        assertNotNull(response);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(dto, response.getBody());
        verify(listService, times(1)).getListById(id);
    }

    @Test
    void getListById_notFound_returnsNotFound() {
        Long id = 2L;

        when(listService.getListById(id)).thenReturn(Optional.empty());

        ResponseEntity<ListDTO> response = listController.getListById(id);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(listService, times(1)).getListById(id);
    }

    @Test
    void createList_returnsCreatedDto() {
        ListDTO input = mock(ListDTO.class);
        ListDTO created = mock(ListDTO.class);

        when(listService.createList(input)).thenReturn(created);

        ListDTO result = listController.createList(input);

        assertNotNull(result);
        assertEquals(created, result);
        verify(listService, times(1)).createList(input);
    }

    @Test
    void updateList_delegatesToService_andReturnsServiceResponse() {
        Long id = 3L;
        ListDTO updated = mock(ListDTO.class);
        ResponseEntity<ListDTO> serviceResponse = ResponseEntity.ok(updated);

        when(listService.updateList(id, updated)).thenReturn(serviceResponse);

        ResponseEntity<ListDTO> response = listController.updateList(id, updated);

        assertNotNull(response);
        assertEquals(serviceResponse, response);
        verify(listService, times(1)).updateList(id, updated);
    }

    @Test
    void deleteList_delegatesToService_andReturnsServiceResponse() {
        Long id = 4L;
        ResponseEntity<Void> serviceResponse = ResponseEntity.noContent().build();

        when(listService.deleteList(id)).thenReturn(serviceResponse);

        ResponseEntity<Void> response = listController.deleteList(id);

        assertNotNull(response);
        assertEquals(serviceResponse, response);
        verify(listService, times(1)).deleteList(id);
    }
}