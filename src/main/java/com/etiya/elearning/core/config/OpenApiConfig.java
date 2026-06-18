package com.etiya.elearning.core.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI elearningOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("E-Learning Platform API")
                .version("v1")
                .description("Online eğitim platformu backend REST API'si (N-Layered mimari).")
                .contact(new Contact().name("Etiya Akademi")));
    }
}
