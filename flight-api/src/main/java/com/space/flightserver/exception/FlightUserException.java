package com.space.flightserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class FlightUserException extends ResponseStatusException {
    public FlightUserException(HttpStatus status) {
        super(status);
    }

    public FlightUserException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public FlightUserException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }

    public FlightUserException(int rawStatusCode, String reason, Throwable cause) {
        super(rawStatusCode, reason, cause);
    }
}
