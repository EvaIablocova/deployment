package com.example.api_gateway.Controller;

import com.example.api_gateway.DTOs.UserDTO;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api_gateway/users")
public class UserController {

    private final RestTemplate restTemplate;
    private final String externalBase = "http://usermicroservice:9015/api/users";

    public UserController(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        try {
            ResponseEntity<UserDTO[]> response = restTemplate.getForEntity(externalBase, UserDTO[].class);
            UserDTO[] body = response.getBody();
            return Arrays.asList(Optional.ofNullable(body).orElse(new UserDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    @GetMapping("/username/{id}")
    public Optional<String> getUsernameById(@PathVariable Long id) {
        try {
            ResponseEntity<UserDTO> response =
                    restTemplate.getForEntity(externalBase + "/" + id, UserDTO.class);

            return Optional.ofNullable(response.getBody())
                    .map(UserDTO::getUsername);

        } catch (RestClientException e) {
            return Optional.empty();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        try {
            ResponseEntity<UserDTO> response =
                    restTemplate.getForEntity(externalBase + "/" + id, UserDTO.class);
            return ResponseEntity.ok(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO taskDTO) {
        try {
            ResponseEntity<UserDTO> response =
                    restTemplate.postForEntity(externalBase, taskDTO, UserDTO.class);
            return ResponseEntity.status(HttpStatus.CREATED).body(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO updatedUserDTO) {
        try {
            restTemplate.put(externalBase + "/" + id, updatedUserDTO);
            return ResponseEntity.ok(updatedUserDTO);
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            restTemplate.delete(externalBase + "/" + id);
            return ResponseEntity.noContent().build();
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }


}