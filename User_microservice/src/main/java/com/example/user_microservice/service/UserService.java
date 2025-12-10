package com.example.user_microservice.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.example.user_microservice.DTOs.UserDTO;
import com.example.user_microservice.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public Optional<UserDTO> getUserById(Long id) {
        return Optional.ofNullable(userRepository.getUserById(id))
                .map(response -> response.getBody());
    }

    public UserDTO createUser(UserDTO UserDTO) {
        return userRepository.createUser(UserDTO).getBody();
    }

    public ResponseEntity<UserDTO> updateUser(Long id, UserDTO updatedUserDTO) {
        return userRepository.updateUser(id, updatedUserDTO);
    }

    public ResponseEntity<Void> deleteUser(Long id) {
        return userRepository.deleteUser(id);
    }


}