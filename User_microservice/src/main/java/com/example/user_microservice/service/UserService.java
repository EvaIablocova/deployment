package com.example.user_microservice.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.example.user_microservice.DTOs.UserDTO;
import com.example.user_microservice.repository.UserRepository;

@Service
public class UserService {

    //test changes

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

    public Optional<Long> getGroupIdByUserId(Long userId) {
        return getUserById(userId)
                .map(UserDTO::getGroupId);
    }

    public UserDTO createUser(UserDTO UserDTO) {
        return userRepository.createUser(UserDTO).getBody();
    }

    public boolean recalculateUserPointsScore(Long userId, int points, boolean isDone) {
        return userRepository.recalculateUserPointsScore(userId, points, isDone);
    }

    public ResponseEntity<UserDTO> updateUser(Long id, UserDTO updatedUserDTO) {
        return userRepository.updateUser(id, updatedUserDTO);
    }

    public ResponseEntity<Void> deleteUser(Long id) {
        return userRepository.deleteUser(id);
    }

    public ResponseEntity<UserDTO> upgradeToPremium(Long id) {
        Optional<UserDTO> userOptional = getUserById(id);

        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        UserDTO oldUserDTO = userOptional.get();
        oldUserDTO.setRole("PREMIUM_USER");
        return updateUser(id, oldUserDTO);
    }

    public ResponseEntity<UserDTO> cancelSubscribtion (Long id) {
        Optional<UserDTO> userOptional = getUserById(id);

        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        UserDTO oldUserDTO = userOptional.get();
        oldUserDTO.setRole("USER");
        return updateUser(id, oldUserDTO);
    }

}