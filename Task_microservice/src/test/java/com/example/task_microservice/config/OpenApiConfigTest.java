package com.example.task_microservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

public class OpenApiConfigTest {

    @Test
    void customOpenAPI_returnsOpenAPIWithCorrectInfo() {
        OpenApiConfig config = new OpenApiConfig();
        OpenAPI openAPI = config.customOpenAPI();

        assertNotNull(openAPI, "OpenAPI should not be null");
        assertNotNull(openAPI.getInfo(), "Info should not be null");
        assertEquals("House_Mate_Microservices Task API", openAPI.getInfo().getTitle());
        assertEquals("3.0", openAPI.getInfo().getVersion());
        assertEquals("API documentation for HouseMate project", openAPI.getInfo().getDescription());
    }

    @Test
    void customOpenAPI_methodIsAnnotatedWithBean() throws NoSuchMethodException {
        Method method = OpenApiConfig.class.getMethod("customOpenAPI");
        assertTrue(method.isAnnotationPresent(Bean.class), "customOpenAPI should be annotated with @Bean");
    }
}