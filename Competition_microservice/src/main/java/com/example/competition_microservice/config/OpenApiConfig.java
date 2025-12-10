package com.example.competition_microservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//http://localhost:9011/swagger-ui/index.html

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("House_Mate_Microservices Competition API")
                        .version("3.0")
                        .description("API documentation for HouseMate project"));
    }
}
