package com.space.flightserver.service.inner;

import com.space.flightserver.model.entity.inner.Astronaut;
import com.space.flightserver.model.entity.inner.Expedition;
import com.space.flightserver.model.entity.inner.Spacecraft;
import com.space.flightserver.model.entity.inner.dto.ExpeditionResponse;
import com.space.flightserver.model.entity.inner.request.CreateExpeditionRequest;
import com.space.flightserver.repository.AstronautRepository;
import com.space.flightserver.repository.ExpeditionRepository;
import com.space.flightserver.repository.SpacecraftRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class ExpeditionServiceTest {

    private ExpeditionService service;

    private ExpeditionRepository repository;
    private SpacecraftRepository spacecraftRepository;
    private AstronautRepository astronautRepository;

    @BeforeEach
    void setUp() {
        repository = mock(ExpeditionRepository.class);
        spacecraftRepository = mock(SpacecraftRepository.class);
        astronautRepository = mock(AstronautRepository.class);
        service = new ExpeditionService(repository, spacecraftRepository, astronautRepository);
    }

    @Test
    void create() {
        Long id = new Random().nextLong(0, Long.MAX_VALUE);
        String mission = "TEST";
        Instant planStart = Instant.now().plusSeconds(1 * 24 * 60 * 60);
        Instant planComplete = Instant.now().plusSeconds(2 * 24 * 60 * 60);
        Spacecraft spacecraft = new Spacecraft("test", true);
        Astronaut astronaut = new Astronaut("test", false);

        CreateExpeditionRequest request = new CreateExpeditionRequest(mission,
                planStart,
                planComplete,
                1L,
                Set.of(1L));

        when(spacecraftRepository.findById(notNull())).thenReturn(Optional.of(spacecraft));
        when(astronautRepository.findAllById(Set.of(1L))).thenReturn(List.of(astronaut));
        when(repository.save(notNull())).thenAnswer(invocation -> {
            Expedition entity = invocation.getArgument(0);
            assertThat(entity.getId()).isNull();
            assertThat(entity.getActualStartDate()).isNull();
            assertThat(entity.getActualCompletionDate()).isNull();
            assertThat(entity.getPlanStartDate()).isEqualTo(planStart);
            assertThat(entity.getPlanCompletionDate()).isEqualTo(planComplete);
            assertThat(entity.getMission()).isEqualTo(mission);
            assertThat(entity.getAstronauts()).isEqualTo(Set.of(astronaut));
            assertThat(entity.getSpacecraft()).isEqualTo(spacecraft);
            entity.setId(id);
            return entity;
        });

        ExpeditionResponse response = service.create(request);
        assertThat(response.id()).isEqualTo(id);
        assertThat(response.mission()).isEqualTo(mission);
        assertThat(response.actualStartDate()).isNull();
        assertThat(response.actualCompletionDate()).isNull();
        assertThat(response.planStartDate()).isEqualTo(planStart);
        assertThat(response.planCompletionDate()).isEqualTo(planComplete);
        verify(repository).save(notNull());

        verifyNoMoreInteractions(repository);
    }

    @Test
    void getById() {
        Long wrongId = new Random().nextLong(0, Long.MAX_VALUE);
        Long realId = new Random().nextLong(0, Long.MAX_VALUE);
        String mission = "TEST";
        Instant planStart = Instant.now().plusSeconds(1 * 24 * 60 * 60);
        Instant planComplete = Instant.now().plusSeconds(2 * 24 * 60 * 60);
        Spacecraft spacecraft = new Spacecraft("test", true);
        Astronaut astronaut = new Astronaut("test", false);

        Expedition expedition = new Expedition(realId,
                mission,
                planStart,
                planComplete,
                spacecraft,
                Set.of(astronaut));

        when(repository.findById(wrongId)).thenReturn(Optional.empty());
        when(repository.findById(realId)).thenReturn(Optional.of(expedition));

        Optional<ExpeditionResponse> emptyResponse = service.getById(wrongId);
        assertThat(emptyResponse).isEmpty();
        verify(repository).findById(wrongId);

        assertThat(service.getById(realId)).hasValueSatisfying(response -> {
            assertThat(response.id()).isEqualTo(realId);
            assertThat(response.mission()).isEqualTo(mission);
            assertThat(response.actualStartDate()).isNull();
            assertThat(response.actualCompletionDate()).isNull();
            assertThat(response.planStartDate()).isEqualTo(planStart);
            assertThat(response.planCompletionDate()).isEqualTo(planComplete);
        });
        verify(repository).findById(realId);

        verifyNoMoreInteractions(repository);
    }
}