package com.space.flightserver.service.serviceinterface.inner;

import com.space.flightserver.model.entity.inner.dto.AstronautResponse;
import com.space.flightserver.model.entity.inner.request.CreateAstronautRequest;

public interface AstronautCRUD extends ServiceCRUD<CreateAstronautRequest, AstronautResponse> {

    void updateState(Long id, Boolean state);

}
