package com.space.flightserver.service;

import com.space.flightserver.exception.SpacecraftException;
import com.space.flightserver.model.dto.SpacecraftResponse;
import com.space.flightserver.model.entity.Spacecraft;
import com.space.flightserver.model.request.CreateSpacecraftRequest;
import com.space.flightserver.repository.SpacecraftRepository;
import com.space.flightserver.service.cruditerface.SpacecraftCRUD;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class SpacecraftService implements SpacecraftCRUD {

    private final SpacecraftRepository repository;

    public SpacecraftService(SpacecraftRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<SpacecraftResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(SpacecraftResponse::fromSpacecraft);
    }

    @Override
    public SpacecraftResponse create(CreateSpacecraftRequest request) {
        var spacecraft = new Spacecraft(request.model(), request.enabled());
        return SpacecraftResponse.fromSpacecraft(repository.save(spacecraft));
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<SpacecraftResponse> getById(Long id) {
        return repository.findById(id).map(SpacecraftResponse::fromSpacecraft);
    }

    @Override
    public void update(Long id, CreateSpacecraftRequest request) {
        var spacecraft = repository.findById(id).orElseThrow(() ->
                new SpacecraftException(HttpStatus.NOT_FOUND, "Spacecraft with id " + id + " was not found"));
        spacecraft.setModel(request.model());
        spacecraft.setEnabled(request.enabled());
    }

    @Override
    public void updateState(Long id, Boolean state) {
        var spacecraft = repository.findById(id).orElseThrow(() ->
                new SpacecraftException(HttpStatus.NOT_FOUND, "Spacecraft with id " + id + " was not found"));
        spacecraft.setEnabled(state);
    }

    @Override
    public Optional<SpacecraftResponse> deleteById(Long id) {
        var spacecraft = repository.findById(id);
        spacecraft.ifPresent(repository::delete);
        return spacecraft.map(SpacecraftResponse::fromSpacecraft);
    }
}
