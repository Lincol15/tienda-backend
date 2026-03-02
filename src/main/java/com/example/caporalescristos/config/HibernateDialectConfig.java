package com.example.caporalescristos.config;

import org.springframework.boot.hibernate.autoconfigure.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Fija el dialecto de Hibernate según la URL de la base de datos.
 * En Render (PostgreSQL) evita el error "Unable to determine Dialect without JDBC metadata".
 */
@Configuration
public class HibernateDialectConfig {

    @Bean
    public HibernatePropertiesCustomizer hibernateDialectCustomizer(Environment env) {
        return hibernateProperties -> {
            String url = env.getProperty("spring.datasource.url", "");
            if (url != null && url.contains("postgresql")) {
                hibernateProperties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
            }
        };
    }
}

