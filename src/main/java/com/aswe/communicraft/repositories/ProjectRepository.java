package com.aswe.communicraft.repositories;

import com.aswe.communicraft.models.entities.ProjectEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Integer> {
    Optional<ProjectEntity> findByName(String username);
}
