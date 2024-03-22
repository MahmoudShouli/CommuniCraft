package com.aswe.communicraft.repositories;

import com.aswe.communicraft.models.entities.CraftEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CraftRepository extends JpaRepository<CraftEntity, Integer> {
    CraftEntity findByCraftName(String craftName);
}
