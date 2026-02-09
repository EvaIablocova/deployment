package com.example.recipes_microservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI recipesOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Recipes Microservice API")
                        .description("API for managing recipes, ingredients, and cooking steps in HouseMate")
                        .version("1.0.0"));
    }
}
