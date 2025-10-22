package com.positivo.podcast.config;

import org.springframework.beans.factory.annotation.Value; // Importar @Value
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // Injeta as origens permitidas do application.properties
    // O ":http://localhost:5173" define um valor padrão caso a propriedade não exista
    @Value("${cors.allowed-origins:http://localhost:5173}")
    private String[] allowedOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // Aplica a todos os endpoints /api/
                .allowedOrigins(allowedOrigins) // Usa as origens injetadas
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}