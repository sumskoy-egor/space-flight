package com.space.flightserver.config.security.properties;

import org.hibernate.validator.constraints.time.DurationMax;
import org.hibernate.validator.constraints.time.DurationMin;

import javax.validation.constraints.NotEmpty;
import java.time.Duration;

public class FlightJWTProperties {

    @NotEmpty
    private char[] secret;

    @DurationMax(minutes = 20)
    @DurationMin(minutes = 1)
    private Duration accessExpireIn;

    @DurationMax(days = 3)
    @DurationMin(hours = 6)
    private Duration refreshExpireIn;

    public char[] getSecret() {
        return secret;
    }

    public void setSecret(char[] secret) {
        this.secret = secret;
    }

    public Duration getAccessExpireIn() {
        return accessExpireIn;
    }

    public void setAccessExpireIn(Duration accessExpireIn) {
        this.accessExpireIn = accessExpireIn;
    }

    public Duration getRefreshExpireIn() {
        return refreshExpireIn;
    }

    public void setRefreshExpireIn(Duration refreshExpireIn) {
        this.refreshExpireIn = refreshExpireIn;
    }
}
