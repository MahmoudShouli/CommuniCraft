package com.aswe.communicraft.repositories;

import com.aswe.communicraft.models.entities.CraftEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CraftRepository extends JpaRepository<CraftEntity, Integer> {
    CraftEntity findByName(String username);
}
