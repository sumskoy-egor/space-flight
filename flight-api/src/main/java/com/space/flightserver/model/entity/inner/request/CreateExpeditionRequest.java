package com.space.flightserver.model.entity.inner.request;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Set;

public record CreateExpeditionRequest(
        String mission,
        @Future Instant startDate,
        @Future Instant completionDate,
        @NotNull Long spacecraft_id,
        @NotEmpty Set<Long> astronauts_id
) {
}
