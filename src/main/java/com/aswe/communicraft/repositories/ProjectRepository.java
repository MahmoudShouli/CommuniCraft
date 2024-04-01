package com.aswe.communicraft.repositories;

import com.aswe.communicraft.models.entities.ProjectEntity;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Integer> {
    Optional<ProjectEntity> findByName(String username);
    @Modifying
    @Transactional
    @Query("SELECT p FROM ProjectEntity p WHERE p.isFinished = true")
    Optional<List<ProjectEntity>> findAllFinishedProjects();
}
