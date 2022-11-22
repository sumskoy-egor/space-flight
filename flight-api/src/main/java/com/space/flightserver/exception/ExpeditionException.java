package com.space.flightserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ExpeditionException extends ResponseStatusException {
    public ExpeditionException(HttpStatus status) {
        super(status);
    }

    public ExpeditionException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public ExpeditionException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }

    public ExpeditionException(int rawStatusCode, String reason, Throwable cause) {
        super(rawStatusCode, reason, cause);
    }
}
