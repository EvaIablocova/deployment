package com.example.user_microservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OpenApiConfigTest {

    @Test
    void customOpenAPI_shouldReturnOpenAPIWithExpectedInfo() {
        OpenApiConfig config = new OpenApiConfig();
        OpenAPI openAPI = config.customOpenAPI();

        assertNotNull(openAPI, "OpenAPI should not be null");
        Info info = openAPI.getInfo();
        assertNotNull(info, "Info should not be null");

        assertEquals("House_Mate_Microservices User API", info.getTitle(), "Title should match");
        assertEquals("3.0", info.getVersion(), "Version should match");
        assertEquals("API documentation for HouseMate project", info.getDescription(), "Description should match");
    }
}