package com.aswe.communicraft.services;

import com.aswe.communicraft.exceptions.NotFoundException;
import com.aswe.communicraft.models.dto.TaskDto;
import jakarta.servlet.http.HttpServletRequest;

public interface TaskService {

    void createTask(TaskDto taskDto, String name, HttpServletRequest request) throws NotFoundException;

    void assignTask(TaskDto taskDto, String userName, HttpServletRequest request) throws NotFoundException;

    void finishTask(String taskName, HttpServletRequest request) throws NotFoundException;
}
