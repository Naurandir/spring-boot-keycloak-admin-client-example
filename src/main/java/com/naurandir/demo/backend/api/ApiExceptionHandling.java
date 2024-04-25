package com.naurandir.demo.backend.api;

import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.naurandir.demo.backend.api.user.UserActionException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
@ResponseBody
public class ApiExceptionHandling {
    
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(UserActionException.class)
    ApiError handleError(UserActionException ex) {
        log.error("handleError: the user was not allowed to perform the requested action, error was: [{}]", ex.getMessage());
        return new ApiError(ex.getCode(), ex.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    ApiError handleError(Exception ex) {
        log.error("handleError: unexpected error happened: ", ex);
        return new ApiError("unexpected", ex.getMessage());
    }
}
