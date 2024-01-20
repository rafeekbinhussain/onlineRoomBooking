package com.conference.online.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class BaseController {

    protected <T> ResponseEntity<T> createResponseEntity(HttpStatus status, T body) {
        return ResponseEntity.status(status).body(body);
    }

    protected ErrorResponse createErrorResponse(HttpStatus status, String message, Object details) {
        return new ErrorResponse(status.value(), message, details);
    }

    public record ErrorResponse(int status, String message, Object details) {
    }
}
