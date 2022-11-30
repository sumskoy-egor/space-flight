package com.space.flightserver.service;

import com.space.flightserver.exception.AstronautException;
import com.space.flightserver.model.entity.inner.Astronaut;
import com.space.flightserver.model.entity.inner.dto.AstronautResponse;
import com.space.flightserver.model.entity.inner.request.CreateAstronautRequest;
import com.space.flightserver.repository.AstronautRepository;
import com.space.flightserver.service.cruditerface.AstronautCRUD;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class AstronautService implements AstronautCRUD {

    private final AstronautRepository repository;

    public AstronautService(AstronautRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<AstronautResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(AstronautResponse::fromAstronaut);
    }

    @Override
    public AstronautResponse create(CreateAstronautRequest request) {
        var astronaut = new Astronaut(request.name(), request.isBusy());
        return AstronautResponse.fromAstronaut(repository.save(astronaut));
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<AstronautResponse> getById(Long id) {
        return repository.findById(id).map(AstronautResponse::fromAstronaut);
    }

    @Override
    public void update(Long id, CreateAstronautRequest request) {
        var astronaut = repository.findById(id).orElseThrow(() -> new AstronautException(HttpStatus.NOT_FOUND,
                "Astronaut with id " + id + "was not found"));

        astronaut.setName(request.name());
        astronaut.setBusy(request.isBusy());
    }

    @Override
    public void updateState(Long id, Boolean request) {
        var astronaut = repository.findById(id).orElseThrow(() -> new AstronautException(HttpStatus.NOT_FOUND,
                "Astronaut with id " + id + "was not found"));

        astronaut.setBusy(request);
    }

    @Override
    public Optional<AstronautResponse> deleteById(Long id) {
        var astronaut = repository.findById(id);
        astronaut.ifPresent(repository::delete);
        return astronaut.map(AstronautResponse::fromAstronaut);
    }
}
