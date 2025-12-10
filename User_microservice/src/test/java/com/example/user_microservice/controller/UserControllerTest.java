package com.example.user_microservice.controller;

import com.example.user_microservice.DTOs.UserDTO;
import com.example.user_microservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void getAllUsers_returnsList_and_callsServiceTwice() {
        UserDTO u1 = mock(UserDTO.class);
        UserDTO u2 = mock(UserDTO.class);
        List<UserDTO> users = Arrays.asList(u1, u2);

        when(userService.getAllUsers()).thenReturn(users);

        List<UserDTO> result = userController.getAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertSame(users, result);

        // Current controller implementation calls userService.getAllUsers() twice; verify that behavior
        verify(userService, times(2)).getAllUsers();
    }

    @Test
    void getTaskById_found_returnsOkWithBody() {
        UserDTO user = mock(UserDTO.class);
        when(userService.getUserById(1L)).thenReturn(Optional.of(user));

        ResponseEntity<UserDTO> response = userController.getTaskById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(user, response.getBody());
        verify(userService).getUserById(1L);
    }

    @Test
    void getTaskById_notFound_returnsNotFound() {
        when(userService.getUserById(1L)).thenReturn(Optional.empty());

        ResponseEntity<UserDTO> response = userController.getTaskById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService).getUserById(1L);
    }

    @Test
    void createTask_delegatesToService_andReturnsCreatedDto() {
        UserDTO input = mock(UserDTO.class);
        UserDTO created = mock(UserDTO.class);
        when(userService.createUser(any(UserDTO.class))).thenReturn(created);

        UserDTO result = userController.createTask(input);

        assertSame(created, result);
        verify(userService).createUser(input);
    }

    @Test
    void updateUser_delegatesToService_andReturnsResponse() {
        UserDTO update = mock(UserDTO.class);
        UserDTO updated = mock(UserDTO.class);
        ResponseEntity<UserDTO> serviceResponse = ResponseEntity.ok(updated);
        when(userService.updateUser(eq(1L), any(UserDTO.class))).thenReturn(serviceResponse);

        ResponseEntity<UserDTO> response = userController.updateUser(1L, update);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(updated, response.getBody());
        verify(userService).updateUser(1L, update);
    }

    @Test
    void deleteUser_delegatesToService_andReturnsResponse() {
        ResponseEntity<Void> serviceResponse = ResponseEntity.noContent().build();
        when(userService.deleteUser(1L)).thenReturn(serviceResponse);

        ResponseEntity<Void> response = userController.deleteUser(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService).deleteUser(1L);
    }
}