package com.space.flightserver.service.cruditerface;

import com.space.flightserver.model.dto.ExpeditionResponse;
import com.space.flightserver.model.request.CreateExpeditionRequest;

public interface ExpeditionCRUD extends ServiceCRUD<CreateExpeditionRequest, ExpeditionResponse> {

    void updateStartExpedition(Long id);

    void updateCompleteExpedition(Long id);
}
