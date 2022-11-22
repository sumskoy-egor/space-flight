package com.space.flightserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AstronautException extends ResponseStatusException {

    public AstronautException(HttpStatus status) {
        super(status);
    }

    public AstronautException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public AstronautException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }

    public AstronautException(int rawStatusCode, String reason, Throwable cause) {
        super(rawStatusCode, reason, cause);
    }
}
