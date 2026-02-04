package com.example.database_microservice.service;

import com.example.database_microservice.DTOs.UserDTO;
import com.example.database_microservice.model.User;
import com.example.database_microservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // --- Mapping ---
    private UserDTO toDTO(User user) {
        return new UserDTO(user);
    }

    // --- CRUD ---
    public List<UserDTO> getAllUsers() {
//        var users = userRepository.findAll();
        return userRepository.findAll().stream().map(this::toDTO).toList();
//        return new ArrayList<>();
    }

    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findById(id).map(this::toDTO);
    }

    public Optional<User> getUserEntityById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<UserDTO> getUserByUsername(String username) {
        return userRepository.findByUsername(username).map(this::toDTO);
    }

    public UserDTO createUser(UserDTO UserDTO) {
        User user = new User(UserDTO);
        return toDTO(userRepository.save(user));
    }

    public Optional<UserDTO> updateUser(Long id, UserDTO updatedUserDTO) {
        User updateduser = new User(updatedUserDTO);
        return userRepository.findById(id).map(user -> {
            user.setUsername(updateduser.getUsername());
            user.setPassword(updateduser.getPassword());
            return toDTO(userRepository.save(user));
        });
    }

    public boolean recalculateUserPointsScore(Long userId, int points, boolean isDone) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty()) {
            return false;
        }

        User user = userOptional.get();
        int currentPoints = user.getPointsScore() != 0 ? user.getPointsScore() : 0;

        if (isDone) {
            currentPoints += points;
        } else {
            currentPoints -= points;
            if (currentPoints < 0) {
                currentPoints = 0;
            }
        }

        user.setPointsScore(currentPoints);
        userRepository.save(user);
        return true;
    }

    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

}

