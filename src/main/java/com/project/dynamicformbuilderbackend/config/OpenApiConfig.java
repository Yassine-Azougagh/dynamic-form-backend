package com.project.dynamicformbuilderbackend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "My API Documentation",
                version = "1.0",
                description = "API documentation for My Spring Boot Application"
        )
)
public class OpenApiConfig {
}