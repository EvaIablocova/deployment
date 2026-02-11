package com.example.user_microservice.controller;

import com.example.user_microservice.DTOs.UserDTO;
import com.example.user_microservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getTaskById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{userId}/groupId")
    public ResponseEntity<Long> getGroupIdByUserId(@PathVariable Long userId) {
        return userService.getGroupIdByUserId(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public UserDTO createTask(@RequestBody UserDTO UserDTO) {
        return userService.createUser(UserDTO);
    }

    @PutMapping("/recalculateUserPointsScore/{userId}/{points}/{isDone}")
    public ResponseEntity<Void> recalculateUserPointsScore(@PathVariable Long userId, @PathVariable int points, @PathVariable boolean isDone) {
        return userService.recalculateUserPointsScore(userId, points, isDone)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO updatedUserDTO) {
        return userService.updateUser(id, updatedUserDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

    @PutMapping("/{id}/upgrade-to-premium")
    public ResponseEntity<UserDTO> upgradeToPremium(@PathVariable Long id) {
         return userService.upgradeToPremium(id);
    }

    @PutMapping("/{id}/cancel-subscribtion")
    public ResponseEntity<UserDTO> cancelSubscribtion(@PathVariable Long id) {
        return userService.cancelSubscribtion(id);
    }

}