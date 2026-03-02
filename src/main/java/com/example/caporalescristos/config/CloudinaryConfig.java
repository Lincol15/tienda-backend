package com.example.caporalescristos.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuración de Cloudinary. Credenciales SOLO desde variables de entorno (Render).
 * NO hardcodear en código ni subir a GitHub.
 */
@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        String cloudName = System.getenv("CLOUDINARY_CLOUD_NAME");
        String apiKey = System.getenv("CLOUDINARY_API_KEY");
        String apiSecret = System.getenv("CLOUDINARY_API_SECRET");
        Map<String, Object> config = new HashMap<>();
        config.put("cloud_name", cloudName != null ? cloudName : "");
        config.put("api_key", apiKey != null ? apiKey : "");
        config.put("api_secret", apiSecret != null ? apiSecret : "");
        config.put("secure", true);
        return new Cloudinary(config);
    }
}
