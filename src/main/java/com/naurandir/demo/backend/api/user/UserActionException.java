package com.naurandir.demo.backend.api.user;

import org.springframework.lang.NonNull;

import lombok.Getter;

@SuppressWarnings("serial")
@Getter
public class UserActionException extends RuntimeException {
    
    private final String code;
    private final String message;
    
    public UserActionException(@NonNull String code, @NonNull String message, Object... messageParameters) {
        this.code = code;
        this.message = message.formatted(messageParameters);
    }
}
