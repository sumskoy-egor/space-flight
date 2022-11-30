package com.space.flightserver.repository;

import com.space.flightserver.model.entity.inner.Spacecraft;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpacecraftRepository extends JpaRepository<Spacecraft, Long> {
}
