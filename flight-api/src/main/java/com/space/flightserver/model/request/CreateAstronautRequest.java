package com.space.flightserver.model.request;

import javax.validation.constraints.NotNull;

public record CreateAstronautRequest(
        @NotNull String name,
        @NotNull Boolean isBusy
) {
}
