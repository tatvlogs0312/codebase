package org.example.codebase.exception;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ErrorResponse {
    private String timestamp;
    private String message;
    private String errorCode;

    public ErrorResponse (String timestamp, String message) {
        this.message = message;
        this.timestamp = timestamp;
    }
}

