package com.example.user_microservice.service;

import com.example.user_microservice.DTOs.UserDTO;
import com.example.user_microservice.repository.UserRepository;
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
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void getAllUsers_returnsListFromRepository() {
        UserDTO u1 = mock(UserDTO.class);
        UserDTO u2 = mock(UserDTO.class);
        List<UserDTO> users = Arrays.asList(u1, u2);

        when(userRepository.getAllUsers()).thenReturn(users);

        List<UserDTO> result = userService.getAllUsers();

        assertNotNull(result);
        assertSame(users, result);
        verify(userRepository).getAllUsers();
    }

    @Test
    void getUserById_whenRepositoryReturnsResponseWithBody_returnsOptionalWithUser() {
        UserDTO user = mock(UserDTO.class);
        when(userRepository.getUserById(1L)).thenReturn(ResponseEntity.ok(user));

        Optional<UserDTO> result = userService.getUserById(1L);

        assertTrue(result.isPresent());
        assertSame(user, result.get());
        verify(userRepository).getUserById(1L);
    }

    @Test
    void getUserById_whenRepositoryReturnsNull_returnsEmptyOptional() {
        when(userRepository.getUserById(1L)).thenReturn(null);

        Optional<UserDTO> result = userService.getUserById(1L);

        assertFalse(result.isPresent());
        verify(userRepository).getUserById(1L);
    }

    @Test
    void createUser_returnsCreatedBody() {
        UserDTO input = mock(UserDTO.class);
        UserDTO created = mock(UserDTO.class);
        when(userRepository.createUser(any(UserDTO.class))).thenReturn(ResponseEntity.ok(created));

        UserDTO result = userService.createUser(input);

        assertSame(created, result);
        verify(userRepository).createUser(input);
    }

    @Test
    void updateUser_delegatesToRepository_andReturnsResponse() {
        UserDTO updated = mock(UserDTO.class);
        ResponseEntity<UserDTO> repoResponse = ResponseEntity.ok(updated);
        when(userRepository.updateUser(1L, updated)).thenReturn(repoResponse);

        ResponseEntity<UserDTO> result = userService.updateUser(1L, updated);

        assertSame(repoResponse, result);
        verify(userRepository).updateUser(1L, updated);
    }

    @Test
    void deleteUser_delegatesToRepository_andReturnsResponse() {
        ResponseEntity<Void> repoResponse = ResponseEntity.noContent().build();
        when(userRepository.deleteUser(1L)).thenReturn(repoResponse);

        ResponseEntity<Void> result = userService.deleteUser(1L);

        assertSame(repoResponse, result);
        verify(userRepository).deleteUser(1L);
    }
}