package com.naurandir.demo.backend.api;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@Configuration
@SecurityScheme(
        name = SpringdocConfig.NAME,
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
        
)
public class SpringdocConfig {
    public static final String NAME = "DemoBearerAuthentication";
}
