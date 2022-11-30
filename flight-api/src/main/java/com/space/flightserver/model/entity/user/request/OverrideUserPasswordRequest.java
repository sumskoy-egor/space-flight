package com.space.flightserver.model.entity.user.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record OverrideUserPasswordRequest(
        @NotBlank(message = "password must not be blank")
        @Size(min = 10, message = "length of your password must be at least 10")
        String password
) {
}
