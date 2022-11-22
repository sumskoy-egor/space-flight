package com.space.flightserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class SpacecraftException extends ResponseStatusException {
    public SpacecraftException(HttpStatus status) {
        super(status);
    }

    public SpacecraftException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public SpacecraftException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }

    public SpacecraftException(int rawStatusCode, String reason, Throwable cause) {
        super(rawStatusCode, reason, cause);
    }
}
