package com.example.reporting_microservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OpenApiConfigTest {

    @Test
    void customOpenAPI_returnsOpenAPIWithExpectedInfo() {
        OpenApiConfig config = new OpenApiConfig();
        OpenAPI openAPI = config.customOpenAPI();

        assertNotNull(openAPI, "OpenAPI should not be null");
        assertNotNull(openAPI.getInfo(), "OpenAPI.info should not be null");

        assertEquals("House_Mate_Microservices Reporting API", openAPI.getInfo().getTitle());
        assertEquals("3.0", openAPI.getInfo().getVersion());
        assertEquals("API documentation for HouseMate project", openAPI.getInfo().getDescription());
    }
}