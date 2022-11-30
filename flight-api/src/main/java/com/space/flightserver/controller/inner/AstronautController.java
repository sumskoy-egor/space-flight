package com.space.flightserver.controller.inner;

import com.space.flightserver.Routes;
import com.space.flightserver.exception.AstronautException;
import com.space.flightserver.model.entity.inner.dto.AstronautResponse;
import com.space.flightserver.model.entity.inner.request.CreateAstronautRequest;
import com.space.flightserver.service.serviceinterface.inner.AstronautCRUD;
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
@RequestMapping(Routes.ASTRONAUTS)
public class AstronautController {

    private final AstronautCRUD astronautCRUD;

    public AstronautController(AstronautCRUD astronautCRUD) {
        this.astronautCRUD = astronautCRUD;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PageableAsQueryParam
    public Page<AstronautResponse> findAll(@Parameter(hidden = true) Pageable pageable) {
        return astronautCRUD.findAll(pageable);
    }

    @GetMapping(value = ("/{id}"), produces = MediaType.APPLICATION_JSON_VALUE)
    public AstronautResponse get(@PathVariable Long id) {
        return astronautCRUD.getById(id)
                .orElseThrow(() ->
                        new AstronautException(HttpStatus.NOT_FOUND, "Astronaut with id " + id + " was not found"));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public AstronautResponse create(@Valid @RequestBody CreateAstronautRequest createAstronautRequest) {
        return astronautCRUD.create(createAstronautRequest);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@PathVariable Long id, @Valid @RequestBody CreateAstronautRequest createAstronautRequest) {
        astronautCRUD.update(id, createAstronautRequest);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping(value = "/busy/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateState(@PathVariable Long id, @RequestBody Boolean state) {
        astronautCRUD.updateState(id, state);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public AstronautResponse delete(@PathVariable Long id) {
        return astronautCRUD.deleteById(id).orElseThrow(() ->
                        new AstronautException(HttpStatus.NOT_FOUND, "Astronaut with id " + id + " was not found"));
    }
}
