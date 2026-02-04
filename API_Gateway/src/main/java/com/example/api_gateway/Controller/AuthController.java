package com.example.api_gateway.Controller;

import com.example.api_gateway.DTOs.AuthResponse;
import com.example.api_gateway.DTOs.LoginRequest;
import com.example.api_gateway.DTOs.RefreshTokenRequest;
import com.example.api_gateway.DTOs.RegisterRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api_gateway/auth")
public class AuthController {

    private final RestTemplate restTemplate;
    private final String authServiceBase;

    public AuthController(RestTemplateBuilder builder, @Value("${auth.service.url}") String authServiceUrl) {
        this.restTemplate = builder.build();
        this.authServiceBase = authServiceUrl + "/api/auth";
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            ResponseEntity<AuthResponse> response = restTemplate.postForEntity(
                    authServiceBase + "/register",
                    request,
                    AuthResponse.class
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response.getBody());
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (RestClientException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(new ErrorResponse("Auth service unavailable"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            ResponseEntity<AuthResponse> response = restTemplate.postForEntity(
                    authServiceBase + "/login",
                    request,
                    AuthResponse.class
            );
            return ResponseEntity.ok(response.getBody());
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (RestClientException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(new ErrorResponse("Auth service unavailable"));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
        try {
            ResponseEntity<AuthResponse> response = restTemplate.postForEntity(
                    authServiceBase + "/refresh",
                    request,
                    AuthResponse.class
            );
            return ResponseEntity.ok(response.getBody());
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (RestClientException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(new ErrorResponse("Auth service unavailable"));
        }
    }

    // Inner class for error responses
    public record ErrorResponse(String message) {}
}
