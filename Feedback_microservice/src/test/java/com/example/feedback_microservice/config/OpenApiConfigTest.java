package com.example.feedback_microservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OpenApiConfigTest {

    @Test
    void customOpenAPI_providesExpectedInfo() {
        OpenApiConfig config = new OpenApiConfig();
        OpenAPI api = config.customOpenAPI();

        assertNotNull(api, "OpenAPI should not be null");
        Info info = api.getInfo();
        assertNotNull(info, "Info should not be null");
        assertEquals("House_Mate_Microservices Feedback API", info.getTitle());
        assertEquals("3.0", info.getVersion());
        assertEquals("API documentation for HouseMate project", info.getDescription());
    }
}