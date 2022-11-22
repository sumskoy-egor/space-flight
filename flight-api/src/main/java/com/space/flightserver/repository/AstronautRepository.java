package com.space.flightserver.repository;

import com.space.flightserver.model.entity.Astronaut;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AstronautRepository extends JpaRepository<Astronaut, Long> {
}
