package com.space.flightserver.service.serviceinterface.inner;

import com.space.flightserver.model.entity.inner.dto.ExpeditionResponse;
import com.space.flightserver.model.entity.inner.request.CreateExpeditionRequest;

public interface ExpeditionCRUD extends ServiceCRUD<CreateExpeditionRequest, ExpeditionResponse> {

    void updateStartExpedition(Long id);

    void updateCompleteExpedition(Long id);
}
