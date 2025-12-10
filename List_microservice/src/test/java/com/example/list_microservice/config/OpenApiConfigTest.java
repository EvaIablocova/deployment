package com.example.list_microservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@Import(OpenApiConfig.class)
class OpenApiConfigTest {

    @Autowired
    private OpenAPI openAPI;

    @Test
    void customOpenAPI_directInstantiation_returnsExpectedInfo() {
        OpenApiConfig config = new OpenApiConfig();
        OpenAPI api = config.customOpenAPI();

        assertNotNull(api, "OpenAPI should not be null");
        Info info = api.getInfo();
        assertNotNull(info, "Info should not be null");
        assertEquals("House_Mate_Microservices List API", info.getTitle(), "title should match");
        assertEquals("3.0", info.getVersion(), "version should match");
        assertEquals("API documentation for HouseMate project", info.getDescription(), "description should match");
    }

    @Test
    void openApiBeanLoadedInContext_hasExpectedInfo() {
        assertNotNull(openAPI, "OpenAPI bean should be loaded in context");
        Info info = openAPI.getInfo();
        assertNotNull(info, "Info should not be null");
        assertEquals("House_Mate_Microservices List API", info.getTitle());
        assertEquals("3.0", info.getVersion());
        assertEquals("API documentation for HouseMate project", info.getDescription());
    }
}