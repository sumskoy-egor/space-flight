package com.space.flightserver.model.entity.inner.dto;

import com.space.flightserver.model.entity.inner.Spacecraft;

public record SpacecraftResponse(Long id, String model, Boolean enabled) {

    public static SpacecraftResponse fromSpacecraft(Spacecraft spacecraft) {
        return new SpacecraftResponse(
                spacecraft.getId(),
                spacecraft.getModel(),
                spacecraft.getEnabled());
    }
}
