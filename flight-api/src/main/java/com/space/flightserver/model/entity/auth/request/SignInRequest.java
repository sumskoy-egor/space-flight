package com.space.flightserver.model.entity.auth.request;

import com.fasterxml.jackson.annotation.JsonAlias;

public record SignInRequest(
        @JsonAlias("email")
        String login,

        String password
) {
}
