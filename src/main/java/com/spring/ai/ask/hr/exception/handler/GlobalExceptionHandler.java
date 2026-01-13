package com.spring.ai.ask.hr.exception.handler;

import com.spring.ai.ask.hr.exception.InvalidPromptException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InvalidPromptException.class)
    public ResponseEntity<?> handleInvalidPromptException(InvalidPromptException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
