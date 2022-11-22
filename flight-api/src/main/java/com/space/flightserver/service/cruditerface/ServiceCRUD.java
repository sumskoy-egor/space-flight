package com.space.flightserver.service.cruditerface;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ServiceCRUD<Requested, Responded> {

    Page<Responded> findAll(Pageable pageable);

    Responded create(Requested request);

    Optional<Responded> getById(Long id);

    void update(Long id, Requested request);

    Optional<Responded> deleteById(Long id);

}
