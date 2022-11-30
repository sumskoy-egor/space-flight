package com.space.flightserver.controller;

import com.space.flightserver.Routes;
import com.space.flightserver.exception.ExpeditionException;
import com.space.flightserver.model.entity.inner.dto.ExpeditionResponse;
import com.space.flightserver.model.entity.inner.request.CreateExpeditionRequest;
import com.space.flightserver.service.cruditerface.ExpeditionCRUD;
import io.swagger.v3.oas.annotations.Parameter;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(Routes.EXPEDITIONS)
public class ExpeditionController {

    private final ExpeditionCRUD expeditionCRUD;

    public ExpeditionController(ExpeditionCRUD expeditionCRUD) {
        this.expeditionCRUD = expeditionCRUD;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PageableAsQueryParam
    public Page<ExpeditionResponse> findAll(@Parameter(hidden = true) Pageable pageable) {
        return expeditionCRUD.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ExpeditionResponse get(@PathVariable Long id) {
        return expeditionCRUD.getById(id).orElseThrow(() ->
                new ExpeditionException(HttpStatus.NOT_FOUND, "Expedition with id " + id + " was not found"));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ExpeditionResponse create(@Valid @RequestBody CreateExpeditionRequest request) {
        return expeditionCRUD.create(request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@PathVariable Long id, @Valid @RequestBody CreateExpeditionRequest request) {
        expeditionCRUD.update(id, request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/start/{id}")
    public void startExpedition(@PathVariable Long id) {
        expeditionCRUD.updateStartExpedition(id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/complete/{id}")
    public void completeExpedition(@PathVariable Long id) {
        expeditionCRUD.updateCompleteExpedition(id);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ExpeditionResponse deleteById(@PathVariable Long id) {
        return expeditionCRUD.deleteById(id).orElseThrow(() ->
                new ExpeditionException(HttpStatus.NOT_FOUND, "Expedition with id " + id + " does not exist"));
    }
}
