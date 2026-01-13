package com.spring.ai.ask.hr.exception;

public class InvalidPromptException extends RuntimeException {
    public InvalidPromptException(String message) {
        super(message);
    }
}
