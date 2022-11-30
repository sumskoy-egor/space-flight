package com.space.flightserver.model.entity.inner.dto;

import com.space.flightserver.model.entity.inner.Expedition;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

public record ExpeditionResponse(
        Long id,
        String mission,
        Instant planStartDate,
        Instant planCompletionDate,
        Instant actualStartDate,
        Instant actualCompletionDate,
        SpacecraftResponse spacecraft,
        Set<AstronautResponse> astronauts
) {

    public static ExpeditionResponse fromExpedition(Expedition expedition) {
        return new ExpeditionResponse(
                expedition.getId(),
                expedition.getMission(),
                expedition.getPlanStartDate(),
                expedition.getPlanCompletionDate(),
                expedition.getActualStartDate(),
                expedition.getActualCompletionDate(),
                SpacecraftResponse.fromSpacecraft(expedition.getSpacecraft()),
                Set.copyOf(expedition.getAstronauts())
                        .stream().map(AstronautResponse::fromAstronaut).collect(Collectors.toSet())
        );
    }
}
