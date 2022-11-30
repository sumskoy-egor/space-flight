package com.space.flightserver.model.entity.inner.request;

import javax.validation.constraints.NotNull;

public record CreateSpacecraftRequest(@NotNull String model, Boolean enabled) {
}
