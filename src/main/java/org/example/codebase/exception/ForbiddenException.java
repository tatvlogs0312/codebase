package org.example.codebase.exception;

public class ForbiddenException extends RuntimeException {
    private String message;

    public ForbiddenException(String message) {
        super(message);
    }
}
