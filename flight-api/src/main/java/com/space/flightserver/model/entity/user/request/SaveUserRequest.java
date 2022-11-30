package com.space.flightserver.model.entity.user.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record SaveUserRequest(
        @Email(message = "email must be valid")
        @NotNull(message = "email must not be null")
        String email,

        @NotBlank(message = "name must not be blank")
        String name,

        @NotBlank(message = "password must not be blank")
        @Size(min = 10, message = "length of your password must be at least 10")
        String password
) {
}
