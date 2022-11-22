package com.space.flightserver.repository;

import com.space.flightserver.model.entity.Expedition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpeditionRepository extends JpaRepository<Expedition, Long> {
}
