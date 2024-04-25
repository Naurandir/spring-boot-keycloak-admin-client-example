package com.naurandir.demo.backend.keycloak;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class KeycloakConfig {
    
    private final KeycloakProperties keycloakProperties;

    @Bean
    Keycloak keycloak() {
        return KeycloakBuilder.builder()
          .serverUrl(keycloakProperties.getUrl())
          .realm(keycloakProperties.getRealm())
          .clientId(keycloakProperties.getClientId())
          .grantType(OAuth2Constants.PASSWORD)
          .username(keycloakProperties.getUsername())
          .password(keycloakProperties.getPassword())
          .build();
    }
}
