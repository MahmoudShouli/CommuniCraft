package com.aswe.communicraft.repositories;

import com.aswe.communicraft.models.entities.CraftEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CraftRepository extends JpaRepository<CraftEntity, Integer> {
    Optional<CraftEntity> findByName(String username);
}
