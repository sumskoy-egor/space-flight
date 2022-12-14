package com.space.flightserver.service.inner;

import com.space.flightserver.model.entity.inner.Astronaut;
import com.space.flightserver.model.entity.inner.dto.AstronautResponse;
import com.space.flightserver.model.entity.inner.request.CreateAstronautRequest;
import com.space.flightserver.repository.AstronautRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class AstronautServiceTest {

    private AstronautService service;

    private AstronautRepository repository;

    @BeforeEach
    void setUp() {
        repository = mock(AstronautRepository.class);
        service = new AstronautService(repository);
    }

    @Test
    void create() {
        CreateAstronautRequest request = new CreateAstronautRequest("test", true);
        Long id = new Random().nextLong(0, Long.MAX_VALUE);

        when(repository.save(notNull())).thenAnswer(invocation -> {
            Astronaut entity = invocation.getArgument(0);
            assertThat(entity.getId()).isNull();
            assertThat(entity.getExpeditions()).isNull();
            assertThat(entity.getName()).isEqualTo(request.name());
            assertThat(entity.getBusy()).isEqualTo(request.isBusy());
            entity.setId(id);
            return entity;
        });

        AstronautResponse response = service.create(request);
        assertThat(response.id()).isEqualTo(id);
        assertThat(response.name()).isEqualTo(request.name());
        assertThat(response.isBusy()).isEqualTo(request.isBusy());
        verify(repository).save(notNull());

        verifyNoMoreInteractions(repository);
    }

    @Test
    void getById() {
        Long wrongId = new Random().nextLong(0, Long.MAX_VALUE);
        Long realId = new Random().nextLong(0, Long.MAX_VALUE);
        Astronaut astronaut = new Astronaut(realId, "test", true);

        when(repository.findById(wrongId)).thenReturn(Optional.empty());
        when(repository.findById(realId)).thenReturn(Optional.of(astronaut));

        Optional<AstronautResponse> emptyResponse = service.getById(wrongId);
        assertThat(emptyResponse).isEmpty();
        verify(repository).findById(wrongId);

        assertThat(service.getById(realId)).hasValueSatisfying(response -> {
            assertThat(response.id()).isEqualTo(astronaut.getId());
            assertThat(response.name()).isEqualTo(astronaut.getName());
            assertThat(response.isBusy()).isEqualTo(astronaut.getBusy());
        });
        verify(repository).findById(realId);

        verifyNoMoreInteractions(repository);
    }

    @Test
    void update() {
        Long wrongId = new Random().nextLong(0, Long.MAX_VALUE);
        Long realId = new Random().nextLong(0, Long.MAX_VALUE);
        Astronaut astronaut = new Astronaut(realId, "test", true);

        CreateAstronautRequest request = new CreateAstronautRequest("TEST", false);

        when(repository.findById(wrongId)).thenReturn(Optional.empty());
        when(repository.findById(realId)).thenReturn(Optional.of(astronaut));

        assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(() -> service.update(wrongId, request))
                .satisfies(e -> assertThat(e.getStatus()).isSameAs(HttpStatus.NOT_FOUND));
        verify(repository).findById(wrongId);

        service.update(realId, request);
        assertThat(astronaut.getName()).isEqualTo(request.name());
        assertThat(astronaut.getBusy()).isEqualTo(request.isBusy());
        verify(repository).findById(realId);

        verifyNoMoreInteractions(repository);
    }

    @Test
    void updateState() {
        Long wrongId = new Random().nextLong(0, Long.MAX_VALUE);
        Long realId = new Random().nextLong(0, Long.MAX_VALUE);
        Astronaut astronaut = new Astronaut(realId, "test", true);

        Boolean request = false;

        when(repository.findById(wrongId)).thenReturn(Optional.empty());
        when(repository.findById(realId)).thenReturn(Optional.of(astronaut));

        assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(() -> service.updateState(wrongId, request))
                .satisfies(e -> assertThat(e.getStatus()).isSameAs(HttpStatus.NOT_FOUND));
        verify(repository).findById(wrongId);

        service.updateState(realId, request);
        assertThat(astronaut.getBusy()).isEqualTo(request);
        verify(repository).findById(realId);

        verifyNoMoreInteractions(repository);
    }

    @Test
    void deleteById() {
        Long wrongId = new Random().nextLong(0, Long.MAX_VALUE);
        Long realId = new Random().nextLong(0, Long.MAX_VALUE);
        Astronaut astronaut = new Astronaut(realId, "test", true);

        when(repository.findById(wrongId)).thenReturn(Optional.empty());
        when(repository.findById(realId)).thenReturn(Optional.of(astronaut));

        assertThat(service.deleteById(wrongId)).isEmpty();
        verify(repository).findById(wrongId);

        assertThat(service.deleteById(realId)).hasValueSatisfying(response -> {
            assertThat(response.id()).isEqualTo(astronaut.getId());
            assertThat(response.name()).isEqualTo(astronaut.getName());
            assertThat(response.isBusy()).isEqualTo(astronaut.getBusy());
        });
        verify(repository).findById(realId);
        verify(repository).delete(astronaut);

        verifyNoMoreInteractions(repository);
    }
}