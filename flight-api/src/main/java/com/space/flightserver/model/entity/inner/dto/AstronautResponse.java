package com.space.flightserver.model.entity.inner.dto;

import com.space.flightserver.model.entity.inner.Astronaut;

public record AstronautResponse(Long id, String name, Boolean isBusy) {

    public static AstronautResponse fromAstronaut(Astronaut astronaut) {
        return new AstronautResponse(
                astronaut.getId(),
                astronaut.getName(),
                astronaut.getBusy());
    }
}
