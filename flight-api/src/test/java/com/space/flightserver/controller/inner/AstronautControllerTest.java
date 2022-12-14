package com.space.flightserver.controller.inner;

import com.space.flightserver.Routes;
import com.space.flightserver.model.entity.inner.dto.AstronautResponse;
import com.space.flightserver.model.entity.inner.request.CreateAstronautRequest;
import com.space.flightserver.service.serviceinterface.inner.AstronautCRUD;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;
import java.util.Random;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AstronautControllerTest {

    private MockMvc mvc;

    private AstronautCRUD astronautCRUD;

    @BeforeEach
    void setUp() {
        astronautCRUD = mock(AstronautCRUD.class);
        mvc = MockMvcBuilders
                .standaloneSetup(new AstronautController(astronautCRUD))
                .build();
    }

    @Test
    void testGet() throws Exception {
        Long id = new Random().nextLong(0, Long.MAX_VALUE);
        AstronautResponse response = new AstronautResponse(id, "test", true);

        when(astronautCRUD.getById(id)).thenReturn(Optional.of(response));

        String expectedJson = """
                {"id":%d,"name":"test","isBusy":true}
                """.formatted(id);

        mvc.perform(get(Routes.ASTRONAUTS + "/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));

        verify(astronautCRUD, only()).getById(id);
    }

    @Test
    void testEmptyGet() throws Exception {
        Long id = new Random().nextLong(0, Long.MAX_VALUE);

        when(astronautCRUD.getById(id)).thenReturn(Optional.empty());

        mvc.perform(get(Routes.ASTRONAUTS + "/{id}", id))
                .andExpect(status().isNotFound());

        verify(astronautCRUD, only()).getById(id);
    }

    @Test
    void create() throws Exception {
        Long id = new Random().nextLong(0, Long.MAX_VALUE);
        CreateAstronautRequest request = new CreateAstronautRequest("test", true);
        AstronautResponse response = new AstronautResponse(id, "test", true);

        when(astronautCRUD.create(request)).thenReturn(response);

        String expectedJson = """
                {"id":%d,"name":"test","isBusy":true}
                """.formatted(id);

        mvc.perform(post(Routes.ASTRONAUTS)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"name":"test","isBusy":true}
                        """))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));

        verify(astronautCRUD, only()).create(request);
    }

    @Test
    void update() throws Exception {
        Long id = new Random().nextLong(0, Long.MAX_VALUE);
        CreateAstronautRequest request = new CreateAstronautRequest("test", true);

        mvc.perform(put(Routes.ASTRONAUTS + "/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"name":"test","isBusy":true}
                        """))
                .andExpect(status().isNoContent());

        verify(astronautCRUD, only()).update(id, request);
    }

    @Test
    void updateState() throws Exception {
        Long id = new Random().nextLong(0, Long.MAX_VALUE);
        Boolean request = false;

        mvc.perform(put(Routes.ASTRONAUTS + "/busy/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        false
                        """))
                .andExpect(status().isNoContent());

        verify(astronautCRUD, only()).updateState(id, request);
    }

    @Test
    void testDelete() throws Exception {
        Long id = new Random().nextLong(0, Long.MAX_VALUE);
        AstronautResponse response = new AstronautResponse(id, "test", true);

        String expectedJson = """
                {"id":%d,"name":"test","isBusy":true}
                """.formatted(id);

        when(astronautCRUD.deleteById(id))
                .thenReturn(Optional.of(response))
                .thenReturn(Optional.empty());

        mvc.perform(delete(Routes.ASTRONAUTS + "/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        mvc.perform(delete(Routes.ASTRONAUTS + "/{id}", id))
                .andExpect(status().isNotFound());

        verify(astronautCRUD, times(2)).deleteById(id);
        verifyNoMoreInteractions(astronautCRUD);
    }
}