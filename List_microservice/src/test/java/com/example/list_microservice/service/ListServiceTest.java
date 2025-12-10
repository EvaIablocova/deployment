package com.example.list_microservice.service;

import com.example.list_microservice.DTOs.ListDTO;
import com.example.list_microservice.repository.ListRepository;
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
class ListServiceTest {

    @Mock
    private ListRepository listRepository;

    @InjectMocks
    private ListService listService;

    @Test
    void getAllLists_returnsRepositoryResult() {
        ListDTO dto1 = mock(ListDTO.class);
        ListDTO dto2 = mock(ListDTO.class);
        List<ListDTO> expected = Arrays.asList(dto1, dto2);

        when(listRepository.getAllLists()).thenReturn(expected);

        List<ListDTO> result = listService.getAllLists();

        assertNotNull(result);
        assertEquals(expected, result);
        verify(listRepository, times(1)).getAllLists();
    }

    @Test
    void getListById_whenRepositoryReturnsResponseWithBody_returnsOptionalWithBody() {
        Long id = 1L;
        ListDTO dto = mock(ListDTO.class);
        when(listRepository.getListById(id)).thenReturn(ResponseEntity.ok(dto));

        Optional<ListDTO> result = listService.getListById(id);

        assertTrue(result.isPresent());
        assertEquals(dto, result.get());
        verify(listRepository, times(1)).getListById(id);
    }

    @Test
    void getListById_whenRepositoryReturnsNullResponse_returnsEmptyOptional() {
        Long id = 2L;
        when(listRepository.getListById(id)).thenReturn(null);

        Optional<ListDTO> result = listService.getListById(id);

        assertFalse(result.isPresent());
        verify(listRepository, times(1)).getListById(id);
    }

    @Test
    void getListById_whenRepositoryReturnsResponseWithNullBody_returnsEmptyOptional() {
        Long id = 3L;
        when(listRepository.getListById(id)).thenReturn(ResponseEntity.ok(null));

        Optional<ListDTO> result = listService.getListById(id);

        assertFalse(result.isPresent());
        verify(listRepository, times(1)).getListById(id);
    }

    @Test
    void createList_returnsBodyFromRepositoryResponse() {
        ListDTO input = mock(ListDTO.class);
        ListDTO created = mock(ListDTO.class);

        when(listRepository.createList(input)).thenReturn(ResponseEntity.ok(created));

        ListDTO result = listService.createList(input);

        assertNotNull(result);
        assertEquals(created, result);
        verify(listRepository, times(1)).createList(input);
    }

    @Test
    void createList_whenRepositoryReturnsResponseWithNullBody_returnsNull() {
        ListDTO input = mock(ListDTO.class);
        when(listRepository.createList(input)).thenReturn(ResponseEntity.ok(null));

        ListDTO result = listService.createList(input);

        assertNull(result);
        verify(listRepository, times(1)).createList(input);
    }

    @Test
    void updateList_delegatesToRepository_andReturnsResponse() {
        Long id = 4L;
        ListDTO updated = mock(ListDTO.class);
        ResponseEntity<ListDTO> repoResponse = ResponseEntity.ok(updated);

        when(listRepository.updateList(id, updated)).thenReturn(repoResponse);

        ResponseEntity<ListDTO> result = listService.updateList(id, updated);

        assertEquals(repoResponse, result);
        verify(listRepository, times(1)).updateList(id, updated);
    }

    @Test
    void deleteList_delegatesToRepository_andReturnsResponse() {
        Long id = 5L;
        ResponseEntity<Void> repoResponse = ResponseEntity.noContent().build();

        when(listRepository.deleteList(id)).thenReturn(repoResponse);

        ResponseEntity<Void> result = listService.deleteList(id);

        assertEquals(repoResponse, result);
        verify(listRepository, times(1)).deleteList(id);
    }
}