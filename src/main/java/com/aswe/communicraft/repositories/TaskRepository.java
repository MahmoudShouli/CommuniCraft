package com.aswe.communicraft.repositories;

import com.aswe.communicraft.models.entities.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<TaskEntity,Integer> {
    Optional<TaskEntity> findByName(String name);

}
