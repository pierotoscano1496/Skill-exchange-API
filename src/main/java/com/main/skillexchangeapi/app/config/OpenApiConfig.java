package com.main.skillexchangeapi.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI defineOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Skill Exchange API REST")
                        .version("1.1.1")
                        .description("Documentación de la API REST de Skill Exchange"));
    }
}
