package com.levelup.config;

/*
 * WebConfig deshabilitado (comentado) para usar la configuración de SecurityConfig.
 * Si necesitas restaurarlo, descomenta este bloque.
 *
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadsPath = Path.of("uploads").toAbsolutePath().toUri().toString();
        registry.addResourceHandler("/uploads/**").addResourceLocations(uploadsPath);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*") // dev only
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(false); // no credentials with "*"
    }
}
*/
