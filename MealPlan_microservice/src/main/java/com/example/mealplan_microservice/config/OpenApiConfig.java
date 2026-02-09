package com.example.mealplan_microservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI mealPlanOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MealPlan Microservice API")
                        .description("API for managing meal plans in HouseMate")
                        .version("1.0.0"));
    }
}
