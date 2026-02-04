package com.example.auth_microservice.service;

import com.example.auth_microservice.DTOs.*;
import com.example.auth_microservice.security.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthService {

    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;

    @Value("${database.service.url}")
    private String databaseServiceUrl;

    public AuthService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = new BCryptPasswordEncoder(10);
        this.restTemplate = new RestTemplate();
    }

    public AuthResponse register(RegisterRequest request) {
        // Check if user already exists
        try {
            ResponseEntity<UserDTO> existingUser = restTemplate.getForEntity(
                    databaseServiceUrl + "/db/users/username/" + request.getUsername(),
                    UserDTO.class
            );
            if (existingUser.getStatusCode() == HttpStatus.OK) {
                throw new RuntimeException("Username already exists");
            }
        } catch (HttpClientErrorException.NotFound e) {
            // User doesn't exist, proceed with registration
        }

        // Hash password
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        // Create user in database
        UserDTO newUser = new UserDTO();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(hashedPassword);
        newUser.setPointsScore(0);

        ResponseEntity<UserDTO> createdUser = restTemplate.postForEntity(
                databaseServiceUrl + "/db/users",
                newUser,
                UserDTO.class
        );

        UserDTO savedUser = createdUser.getBody();
        if (savedUser == null) {
            throw new RuntimeException("Failed to create user");
        }

        // Generate tokens
        String token = jwtUtil.generateToken(savedUser.getUsername(), savedUser.getId());
        String refreshToken = jwtUtil.generateRefreshToken(savedUser.getUsername(), savedUser.getId());

        return new AuthResponse(token, refreshToken, savedUser.getUsername(), savedUser.getId());
    }

    public AuthResponse login(LoginRequest request) {
        // Get user from database
        ResponseEntity<UserDTO> userResponse;
        try {
            userResponse = restTemplate.getForEntity(
                    databaseServiceUrl + "/db/users/username/" + request.getUsername(),
                    UserDTO.class
            );
        } catch (HttpClientErrorException.NotFound e) {
            throw new RuntimeException("Invalid username or password");
        }

        UserDTO user = userResponse.getBody();
        if (user == null) {
            throw new RuntimeException("Invalid username or password");
        }

        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        // Generate tokens
        String token = jwtUtil.generateToken(user.getUsername(), user.getId());
        String refreshToken = jwtUtil.generateRefreshToken(user.getUsername(), user.getId());

        return new AuthResponse(token, refreshToken, user.getUsername(), user.getId());
    }

    public TokenValidationResponse validateToken(String token) {
        try {
            if (jwtUtil.validateToken(token)) {
                String username = jwtUtil.extractUsername(token);
                Long userId = jwtUtil.extractUserId(token);
                return new TokenValidationResponse(true, username, userId, "Token is valid");
            } else {
                return new TokenValidationResponse(false, null, null, "Token is invalid or expired");
            }
        } catch (Exception e) {
            return new TokenValidationResponse(false, null, null, "Token validation failed: " + e.getMessage());
        }
    }

    public AuthResponse refreshToken(String refreshToken) {
        try {
            if (!jwtUtil.validateToken(refreshToken)) {
                throw new RuntimeException("Invalid refresh token");
            }

            if (!jwtUtil.isRefreshToken(refreshToken)) {
                throw new RuntimeException("Token is not a refresh token");
            }

            String username = jwtUtil.extractUsername(refreshToken);
            Long userId = jwtUtil.extractUserId(refreshToken);

            // Generate new tokens
            String newToken = jwtUtil.generateToken(username, userId);
            String newRefreshToken = jwtUtil.generateRefreshToken(username, userId);

            return new AuthResponse(newToken, newRefreshToken, username, userId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to refresh token: " + e.getMessage());
        }
    }
}
