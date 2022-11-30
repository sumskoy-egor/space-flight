package com.space.flightserver.model.entity.user.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record MergeUserRequest(
        @Email(message = "email must be valid")
        @NotNull(message = "email must not be null")
        String email,

        @NotBlank(message = "name must not be blank")
        String name
) {
}
