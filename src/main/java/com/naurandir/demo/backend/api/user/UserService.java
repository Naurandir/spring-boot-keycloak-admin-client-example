package com.naurandir.demo.backend.api.user;

import java.util.List;

import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserService {
    
    @Value("${com.naurandir.demo.backend.keycloak.realm}")
    private String realm;
    
    @Value("${com.naurandir.demo.backend.keycloak.clientId}")
    private String clientId;
    
    private final Keycloak keycloak;
    
    List<UserDto> getUsers() {
        log.info("try getting users of realm [{}]", realm);
        return getRealm().users().list().stream()
                .map(this::mapUserToDto)
                .toList();
    }
    
    UserDto getUser(String username) {
        log.info("getUser: try finding user by username [{}]", username);
        
        List<UserRepresentation> users =  getRealm().users().searchByUsername(username, true);

        if (users.isEmpty()) {
            throw new UserActionException("user-not-found", "the user with the given username [%s] could not be found", username);
        }
        
        if (users.size() > 1) {
            log.warn("getUser: found multiple users, please check keycloak");
        }
        
        return mapUserToDto(users.get(0));
    }
    
    UserDto createUser(UserDto userDto) {
        log.info("createUser: create user [{}]", userDto);
        
        if (userDto.password() == null || "".equals(userDto.password())) {
            throw new UserActionException("password-missing", "the request does not contain a password, password is required for creation");
        }
        
        String publicId = null;
        try {
            Response response = createKeycloakUser(userDto);
            publicId = CreatedResponseUtil.getCreatedId(response);
            
            setPassword(publicId, userDto.password());
            
            addRealmRole(publicId, "demo-realm");
            
            addClientRole(publicId, "demo-user");
            
            // If email should be verified then this action needs to be executed in order to send out an email for verification
            // keycloak.realm(realm).users().get(publicId).executeActionsEmail(List.of("VERIFY_EMAIL"));
            
            return mapUserToDto(getRealm().users().get(publicId).toRepresentation());
        } catch (Exception ex) {
            if (publicId != null) {
                log.warn("getUser: creation throwed exception [{}] but a user was created, revert creation", ex.getMessage());
                try {
                    deleteUser(publicId);
                } catch (Exception exRollback) {
                    log.warn("getUser: rollback of user creation did not work, but publicId exists there should be a user to delete, excetion: ", exRollback);
                }
            }
            throw ex;
        }
    }
    
    void deleteUser(String publicId) {
        log.info("deleteUser: delete user [{}]", publicId);
        
        UserRepresentation foundUser = getRealm().users().get(publicId).toRepresentation();
        if ("admin".equals(foundUser.getUsername())) {
            throw new UserActionException("delete-not-allowed", "the user with given id [%s] is not allowed to be deleted, it is the admin account", publicId);
        }
        
        getRealm().users().get(publicId).remove();
    }
    
    private RealmResource getRealm() {
        return keycloak.realm(realm);
    }
    
    private Response createKeycloakUser(UserDto userDto) {
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setEmailVerified(true);
        user.setUsername(userDto.username());
        user.setEmail(userDto.email());
        user.setFirstName(userDto.firstName());
        user.setLastName(userDto.lastName());
        
        Response response = getRealm().users().create(user);
        if (response.getStatus() >= Response.Status.BAD_REQUEST.getStatusCode()) {
            throw new UserActionException("user-creation-error", "the user [%s] could not be created, response: %s", userDto.username(), response.getStatusInfo());
        }
        return response;
    }
    
    private String setPassword(String userId, String password) {
        CredentialRepresentation credentials = new CredentialRepresentation();
        credentials.setTemporary(false);
        credentials.setType(CredentialRepresentation.PASSWORD);
        credentials.setValue(password);
        
        getRealm().users().get(userId).resetPassword(credentials);
        return userId;
    }
    
    private void addRealmRole(String userId, String role) {
        RoleRepresentation userRole = getRealm().roles().get(role).toRepresentation();
        getRealm().users().get(userId).roles().realmLevel().add(List.of(userRole));
    }
    
    private void addClientRole(String userId, String role) {
        String internelClientId = getRealm().clients().findByClientId(clientId).get(0).getId();
        
        RoleRepresentation demoUserRole = getRealm().clients().get(internelClientId).roles().get(role).toRepresentation();
        getRealm().users().get(userId).roles().clientLevel(internelClientId).add(List.of(demoUserRole));
    }
    
    private UserDto mapUserToDto(UserRepresentation user) {
        return new UserDto(user.getId(), user.getEmail(), user.getUsername(), user.getFirstName(), user.getLastName(), null);
    }
}
