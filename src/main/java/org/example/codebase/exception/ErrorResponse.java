package org.example.codebase.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
    private String timestamp;
    private String message;

    public ErrorResponse () {

    }

    public ErrorResponse (String timestamp, String message) {
        this.message = message;
        this.timestamp = timestamp;
    }
}

