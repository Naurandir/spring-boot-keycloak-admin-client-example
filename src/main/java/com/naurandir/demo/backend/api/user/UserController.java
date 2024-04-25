package com.naurandir.demo.backend.api.user;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.naurandir.demo.backend.api.SpringdocConfig;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    
    @Operation(summary = "get all users", description = "get the list of all users")
    @GetMapping("/users")
    List<UserDto> getUsers() {
        return userService.getUsers();
    }
    
    @Operation(summary = "get a specific user by username")
    @GetMapping("/users/{username}")
    UserDto getUser(@PathVariable String username) {
        return userService.getUser(username);
    }
    
    @SecurityRequirement(name = SpringdocConfig.NAME)
    @Operation(summary = "create a new user")
    @PostMapping("/users")
    UserDto createUser(@RequestBody UserDto dto) {
        return userService.createUser(dto);
    }
    
    @SecurityRequirement(name = SpringdocConfig.NAME)
    @Operation(summary = "delete an existing user by id")
    @DeleteMapping("/users/{publicId}")
    void deleteUser(@PathVariable String publicId) {
        userService.deleteUser(publicId);
    }
}
