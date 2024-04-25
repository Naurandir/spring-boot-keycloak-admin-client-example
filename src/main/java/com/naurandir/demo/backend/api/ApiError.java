package com.naurandir.demo.backend.api;

import org.springframework.lang.NonNull;

public record ApiError (@NonNull String code, String errorMessage) {
    
}