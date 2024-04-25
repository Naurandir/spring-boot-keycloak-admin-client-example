package com.naurandir.demo.backend.keycloak;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "com.naurandir.demo.backend.keycloak")
public class KeycloakProperties {

    private String url;
    private String realm;
    private String clientId;
    private String username;
    private String password;
}
