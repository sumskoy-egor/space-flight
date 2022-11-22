package com.space.flightserver.controller;

import com.space.flightserver.exception.SpacecraftException;
import com.space.flightserver.model.dto.SpacecraftResponse;
import com.space.flightserver.model.request.CreateSpacecraftRequest;
import com.space.flightserver.service.cruditerface.SpacecraftCRUD;
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
@RequestMapping("/spacecrafts")
public class SpacecraftController {

    private final SpacecraftCRUD spacecraftCRUD;

    public SpacecraftController(SpacecraftCRUD spacecraftCRUD) {
        this.spacecraftCRUD = spacecraftCRUD;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PageableAsQueryParam
    public Page<SpacecraftResponse> findAll(@Parameter(hidden = true) Pageable pageable) {
        return spacecraftCRUD.findAll(pageable);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public SpacecraftResponse get(@PathVariable Long id) {
        return spacecraftCRUD.getById(id).orElseThrow(() ->
                new SpacecraftException(HttpStatus.NOT_FOUND, "Spacecraft with id " + id + " was not found"));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public SpacecraftResponse create(@Valid @RequestBody CreateSpacecraftRequest request) {
        return spacecraftCRUD.create(request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@PathVariable Long id, @Valid @RequestBody CreateSpacecraftRequest request) {
        spacecraftCRUD.update(id, request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping(value = "/state/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateState(@PathVariable Long id, @RequestBody Boolean state) {
        spacecraftCRUD.updateState(id, state);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public SpacecraftResponse deleteById(@PathVariable Long id) {
        return spacecraftCRUD.deleteById(id).orElseThrow(() ->
                new SpacecraftException(HttpStatus.NOT_FOUND, "Spacecraft with id " + id + " was not found"));
    }
}
