package com.virtual_try_backend.shoppingApp.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**") // ✅ Apply CORS to all API endpoints
                        .allowedOrigins("http://127.0.0.1:5500") // ✅ Allow frontend origin
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // ✅ Allow all HTTP methods
                        .allowedHeaders("*") // ✅ Allow all headers
                        .exposedHeaders("Authorization") // ✅ Expose Authorization header
                        .allowCredentials(true) // ✅ Allow authentication cookies/tokens
                        .maxAge(3600); // ✅ Cache preflight request for 1 hour
            }
        };
    }
}
