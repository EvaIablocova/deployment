package com.example.task_microservice.service;

import com.example.task_microservice.DTOs.TaskDTO;
import com.example.task_microservice.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void getAllTasks_returnsListFromRepository() {
        TaskDTO t1 = mock(TaskDTO.class);
        TaskDTO t2 = mock(TaskDTO.class);
        List<TaskDTO> expected = List.of(t1, t2);

        when(taskRepository.getAllTasks()).thenReturn(expected);

        List<TaskDTO> actual = taskService.getAllTasks();

        assertSame(expected, actual);
        verify(taskRepository, times(1)).getAllTasks();
    }

    @Test
    void getTaskById_found_returnsOptionalWithBody() {
        TaskDTO dto = mock(TaskDTO.class);
        when(taskRepository.getTaskById(1L)).thenReturn(ResponseEntity.ok(dto));

        Optional<TaskDTO> result = taskService.getTaskById(1L);

        assertTrue(result.isPresent());
        assertSame(dto, result.get());
        verify(taskRepository).getTaskById(1L);
    }

    @Test
    void getTaskById_responseWithoutBody_returnsEmptyOptional() {
        when(taskRepository.getTaskById(2L)).thenReturn(ResponseEntity.notFound().build());

        Optional<TaskDTO> result = taskService.getTaskById(2L);

        assertFalse(result.isPresent());
        verify(taskRepository).getTaskById(2L);
    }

    @Test
    void createTask_delegatesAndReturnsCreatedBody() {
        TaskDTO input = mock(TaskDTO.class);
        TaskDTO created = mock(TaskDTO.class);
        when(taskRepository.createTask(input)).thenReturn(ResponseEntity.ok(created));

        TaskDTO result = taskService.createTask(input);

        assertSame(created, result);
        verify(taskRepository).createTask(input);
    }

    @Test
    void updateTask_delegatesAndReturnsResponseEntity() {
        TaskDTO updated = mock(TaskDTO.class);
        ResponseEntity<TaskDTO> serviceResp = ResponseEntity.ok(updated);
        when(taskRepository.updateTask(1L, updated)).thenReturn(serviceResp);

        ResponseEntity<TaskDTO> resp = taskService.updateTask(1L, updated);

        assertSame(serviceResp, resp);
        verify(taskRepository).updateTask(1L, updated);
    }

    @Test
    void deleteTask_delegatesAndReturnsResponseEntity() {
        ResponseEntity<Void> serviceResp = ResponseEntity.noContent().build();
        when(taskRepository.deleteTask(5L)).thenReturn(serviceResp);

        ResponseEntity<Void> resp = taskService.deleteTask(5L);

        assertSame(serviceResp, resp);
        verify(taskRepository).deleteTask(5L);
    }

    @Test
    void getTasksByUserId_filtersByUserId() {
        TaskDTO a = mock(TaskDTO.class);
        TaskDTO b = mock(TaskDTO.class);
        when(a.getUserId()).thenReturn(10L);
        when(b.getUserId()).thenReturn(20L);

        List<TaskDTO> all = List.of(a, b);
        when(taskRepository.getAllTasks()).thenReturn(all);

        List<TaskDTO> result = taskService.getTasksByUserId(10L);

        assertEquals(1, result.size());
        assertSame(a, result.get(0));
        verify(taskRepository).getAllTasks();
    }
}