package com.space.flightserver.model.dto;

import com.space.flightserver.model.entity.Spacecraft;

public record SpacecraftResponse(Long id, String model, Boolean enabled) {

    public static SpacecraftResponse fromSpacecraft(Spacecraft spacecraft) {
        return new SpacecraftResponse(
                spacecraft.getId(),
                spacecraft.getModel(),
                spacecraft.getEnabled());
    }
}
