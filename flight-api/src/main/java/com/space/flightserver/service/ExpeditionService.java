package com.space.flightserver.service;

import com.space.flightserver.exception.ExpeditionException;
import com.space.flightserver.model.entity.inner.Astronaut;
import com.space.flightserver.model.entity.inner.Expedition;
import com.space.flightserver.model.entity.inner.dto.ExpeditionResponse;
import com.space.flightserver.model.entity.inner.request.CreateExpeditionRequest;
import com.space.flightserver.repository.AstronautRepository;
import com.space.flightserver.repository.ExpeditionRepository;
import com.space.flightserver.repository.SpacecraftRepository;
import com.space.flightserver.service.cruditerface.ExpeditionCRUD;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class ExpeditionService implements ExpeditionCRUD {

    private final ExpeditionRepository repository;

    private final SpacecraftRepository spacecraftRepository;

    private final AstronautRepository astronautRepository;

    public ExpeditionService(ExpeditionRepository repository,
                             SpacecraftRepository spacecraftRepository,
                             AstronautRepository astronautRepository) {
        this.repository = repository;
        this.spacecraftRepository = spacecraftRepository;
        this.astronautRepository = astronautRepository;
    }

    @Override
    public Page<ExpeditionResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(ExpeditionResponse::fromExpedition);
    }

    @Override
    public ExpeditionResponse create(CreateExpeditionRequest request) {
        var expedition = new Expedition();
        expedition.setMission(request.mission());
        expedition.setPlanStartDate(request.startDate());
        expedition.setPlanCompletionDate(request.completionDate());

        var spacecraft = spacecraftRepository.findById(request.spacecraft_id()).orElseThrow(() ->
                new ExpeditionException(HttpStatus.NOT_FOUND,
                        "Spacecraft with id " + request.spacecraft_id() + " was not found"));
        expedition.setSpacecraft(spacecraft);

        var astronauts = Set.copyOf(astronautRepository.findAllById(request.astronauts_id()));
        if (astronauts.size() != request.astronauts_id().size()) {
            throw new ExpeditionException(HttpStatus.NOT_FOUND, "Unexpected astronaut id");
        }

        expedition.setAstronauts(astronauts);

        return ExpeditionResponse.fromExpedition(repository.save(expedition));
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<ExpeditionResponse> getById(Long id) {
        return repository.findById(id).map(ExpeditionResponse::fromExpedition);
    }

    @Override
    public void update(Long id, CreateExpeditionRequest request) {
        var expedition = repository.findById(id).orElseThrow(() ->
                new ExpeditionException(HttpStatus.NOT_FOUND, "Expedition with id " + id + " was not found"));

        expedition.setPlanCompletionDate(request.completionDate());

        var astronauts = Set.copyOf(astronautRepository.findAllById(request.astronauts_id()));
        if (astronauts.size() != request.astronauts_id().size()) {
            throw new ExpeditionException(HttpStatus.NOT_FOUND, "Unexpected astronaut id");
        }

        expedition.setAstronauts(astronauts);
    }

    @Override
    public void updateStartExpedition(Long id) {
        var expedition = repository.findById(id).orElseThrow(() ->
                new ExpeditionException(HttpStatus.NOT_FOUND, "Expedition with id " + id + " was not found"));

        if (expedition.getActualStartDate() != null) {
            throw new ExpeditionException(HttpStatus.CONFLICT, "Expedition with id " + id + " has already started");
        }

        if(!expedition.getSpacecraft().getEnabled()) {
            throw new ExpeditionException(HttpStatus.CONFLICT,
                    "Spacecraft with id " + expedition.getSpacecraft().getId() + " is enabled");
        } else {
            expedition.getSpacecraft().setEnabled(false);
        }

        if(expedition.getAstronauts().stream().anyMatch(Astronaut::getBusy)) {
            throw new ExpeditionException(HttpStatus.CONFLICT, "Some astronauts are busy");
        } else {
            expedition.getAstronauts().forEach((astronaut) -> astronaut.setBusy(true));
        }

        expedition.setActualStartDate(Instant.now());
    }

    @Override
    public void updateCompleteExpedition(Long id) {
        var expedition = repository.findById(id).orElseThrow(() ->
                new ExpeditionException(HttpStatus.NOT_FOUND, "Expedition with id " + id + " was not found"));

        if (expedition.getActualCompletionDate() != null) {
            throw new ExpeditionException(HttpStatus.CONFLICT, "Expedition with id " + id + " has already completed");
        }

        expedition.getSpacecraft().setEnabled(true);
        expedition.getAstronauts().forEach((astronaut) -> astronaut.setBusy(false));

        expedition.setActualCompletionDate(Instant.now());
    }

    @Override
    public Optional<ExpeditionResponse> deleteById(Long id) {
        var expedition = repository.findById(id);
        expedition.ifPresent(repository::delete);
        return expedition.map(ExpeditionResponse::fromExpedition);
    }
}
