package com.aswe.communicraft.repositories;

import com.aswe.communicraft.models.entities.MaterialEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialRepository extends JpaRepository<MaterialEntity,Integer> {
}
