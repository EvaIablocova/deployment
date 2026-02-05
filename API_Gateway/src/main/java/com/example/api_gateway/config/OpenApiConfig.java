package com.example.api_gateway.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//http://localhost:8099/swagger-ui/index.html

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("House_Mate_Microservices API Gateway")
                        .version("3.0")
                        .description("API documentation for HouseMate project\n\n" +
                                "## Authentication\n" +
                                "1. Use `/api_gateway/auth/register` to create an account\n" +
                                "2. Use `/api_gateway/auth/login` to get a JWT token\n" +
                                "3. Click 'Authorize' button and enter: `Bearer <your-token>`\n" +
                                "4. Now you can access protected endpoints"))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Enter JWT token (without 'Bearer ' prefix)")));
    }
}
