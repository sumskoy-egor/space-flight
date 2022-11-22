package com.space.flightserver.service.cruditerface;

import com.space.flightserver.model.dto.AstronautResponse;
import com.space.flightserver.model.request.CreateAstronautRequest;

public interface AstronautCRUD extends ServiceCRUD<CreateAstronautRequest, AstronautResponse> {

    void updateState(Long id, Boolean state);

}
