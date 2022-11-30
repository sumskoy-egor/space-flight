package com.space.flightserver.service.serviceinterface.inner;

import com.space.flightserver.model.entity.inner.dto.SpacecraftResponse;
import com.space.flightserver.model.entity.inner.request.CreateSpacecraftRequest;

public interface SpacecraftCRUD extends ServiceCRUD<CreateSpacecraftRequest, SpacecraftResponse> {

    void updateState(Long id, Boolean state);

}
