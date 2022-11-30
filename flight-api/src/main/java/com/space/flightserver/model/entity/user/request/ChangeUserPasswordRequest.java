package com.space.flightserver.model.entity.user.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record ChangeUserPasswordRequest(
        @NotNull
        String oldPassword,

        @NotBlank(message = "password must not be blank")
        @Size(min = 10, message = "length of your password must be at least 10")
        String newPassword
) {
}
