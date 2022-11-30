package com.space.flightserver.model.entity.inner.dto;

import com.space.flightserver.model.entity.inner.Expedition;

import java.time.Instant;

public record ExpeditionResponse(
        Long id,
        String mission,
        Instant planStartDate,
        Instant planCompletionDate,
        Instant actualStartDate,
        Instant actualCompletionDate,
        SpacecraftResponse spacecraft
) {

    public static ExpeditionResponse fromExpedition(Expedition expedition) {
        return new ExpeditionResponse(
                expedition.getId(),
                expedition.getMission(),
                expedition.getPlanStartDate(),
                expedition.getPlanCompletionDate(),
                expedition.getActualStartDate(),
                expedition.getActualCompletionDate(),
                SpacecraftResponse.fromSpacecraft(expedition.getSpacecraft()));
    }
}
