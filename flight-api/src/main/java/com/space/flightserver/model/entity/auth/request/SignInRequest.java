package com.space.flightserver.model.entity.auth.request;

import com.fasterxml.jackson.annotation.JsonAlias;

import javax.validation.constraints.Email;

public record SignInRequest(

        @Email
        @JsonAlias("email")
        String login,

        String password
) {
}
