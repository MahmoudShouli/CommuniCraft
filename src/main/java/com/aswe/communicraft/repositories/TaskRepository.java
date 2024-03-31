package com.aswe.communicraft.repositories;

import com.aswe.communicraft.models.entities.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<TaskEntity,Integer> {

}
