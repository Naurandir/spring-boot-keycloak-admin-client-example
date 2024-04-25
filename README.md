# Naurandirs Spring Boot Example for calling Keycloak via admin-client library
An example how to make CRUD operations with Spring Boot Backend against Keycloak by admin client library

# Version
* Spring-Boot: 3.2.4
* Keycloak: 24.0.3

# Notice
* The Keycloak Realm is exported and also part of this repository: realm-export.json
* The Swagger UI is reachable under: http://localhost:8080/swagger-ui/index.html
* As Spring Boot uses Port 8080, the keycloak i used was configured under port 8070
* * The Url of Keycloak, Realm, clientId and so on are set in Spring Boot under src/main/resources/application.properties