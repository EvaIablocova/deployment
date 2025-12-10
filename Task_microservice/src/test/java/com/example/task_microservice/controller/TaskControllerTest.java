package com.example.task_microservice.controller;

import com.example.task_microservice.DTOs.TaskDTO;
import com.example.task_microservice.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskControllerTest {

    private TaskService taskService;
    private TaskController controller;

    @BeforeEach
    void setUp() {
        taskService = mock(TaskService.class);
        controller = new TaskController(taskService);
    }


    @Test
    void getTaskById_found_returnsOkWithBody() {
        TaskDTO dto = mock(TaskDTO.class);
        when(taskService.getTaskById(1L)).thenReturn(Optional.of(dto));

        ResponseEntity<TaskDTO> resp = controller.getTaskById(1L);

        assertEquals(200, resp.getStatusCodeValue());
        assertSame(dto, resp.getBody());
        verify(taskService).getTaskById(1L);
    }

    @Test
    void getTaskById_notFound_returnsNotFound() {
        when(taskService.getTaskById(2L)).thenReturn(Optional.empty());

        ResponseEntity<TaskDTO> resp = controller.getTaskById(2L);

        assertEquals(404, resp.getStatusCodeValue());
        assertNull(resp.getBody());
        verify(taskService).getTaskById(2L);
    }

    @Test
    void createTask_delegatesAndReturnsCreatedDto() {
        TaskDTO input = mock(TaskDTO.class);
        TaskDTO created = mock(TaskDTO.class);
        when(taskService.createTask(input)).thenReturn(created);

        TaskDTO result = controller.createTask(input);

        assertSame(created, result);
        verify(taskService).createTask(input);
    }

    @Test
    void updateTask_delegatesAndReturnsResponseEntity() {
        TaskDTO updated = mock(TaskDTO.class);
        ResponseEntity<TaskDTO> serviceResp = ResponseEntity.ok(updated);
        when(taskService.updateTask(1L, updated)).thenReturn(serviceResp);

        ResponseEntity<TaskDTO> resp = controller.updateTask(1L, updated);

        assertSame(serviceResp, resp);
        verify(taskService).updateTask(1L, updated);
    }

    @Test
    void deleteTask_delegatesAndReturnsResponseEntity() {
        ResponseEntity<Void> serviceResp = ResponseEntity.noContent().build();
        when(taskService.deleteTask(5L)).thenReturn(serviceResp);

        ResponseEntity<Void> resp = controller.deleteTask(5L);

        assertSame(serviceResp, resp);
        verify(taskService).deleteTask(5L);
    }

    @Test
    void getTasksByUserId_returnsListFromService() {
        TaskDTO t = mock(TaskDTO.class);
        List<TaskDTO> expected = List.of(t);
        when(taskService.getTasksByUserId(10L)).thenReturn(expected);

        List<TaskDTO> actual = controller.getTasksByUserId(10L);

        assertEquals(expected, actual);
        verify(taskService).getTasksByUserId(10L);
    }
}