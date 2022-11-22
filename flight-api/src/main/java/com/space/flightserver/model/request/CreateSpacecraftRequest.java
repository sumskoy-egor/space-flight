package com.space.flightserver.model.request;

import javax.validation.constraints.NotNull;

public record CreateSpacecraftRequest(@NotNull String model, Boolean enabled) {
}
