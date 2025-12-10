package com.example.competition_microservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

class OpenApiConfigTest {

    private AnnotationConfigApplicationContext context;

    @AfterEach
    void tearDown() {
        if (context != null) {
            context.close();
        }
    }

    @Test
    void customOpenAPIBeanIsCreated() {
        context = new AnnotationConfigApplicationContext(OpenApiConfig.class);
        OpenAPI openAPI = context.getBean(OpenAPI.class);
        assertNotNull(openAPI, "OpenAPI bean should be present in context");
    }

    @Test
    void customOpenAPIContainsExpectedInfo() {
        context = new AnnotationConfigApplicationContext(OpenApiConfig.class);
        OpenAPI openAPI = context.getBean(OpenAPI.class);
        assertNotNull(openAPI, "OpenAPI bean should be present in context");

        Info info = openAPI.getInfo();
        assertNotNull(info, "Info should not be null");

        assertEquals("House_Mate_Microservices Competition API", info.getTitle(), "Title should match");
        assertEquals("3.0", info.getVersion(), "Version should match");
        assertEquals("API documentation for HouseMate project", info.getDescription(), "Description should match");
    }
}