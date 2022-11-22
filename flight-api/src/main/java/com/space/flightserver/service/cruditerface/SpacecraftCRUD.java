package com.space.flightserver.service.cruditerface;

import com.space.flightserver.model.dto.SpacecraftResponse;
import com.space.flightserver.model.request.CreateSpacecraftRequest;

public interface SpacecraftCRUD extends ServiceCRUD<CreateSpacecraftRequest, SpacecraftResponse> {

    void updateState(Long id, Boolean state);

}
