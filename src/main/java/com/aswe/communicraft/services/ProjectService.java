package com.aswe.communicraft.services;

import com.aswe.communicraft.exceptions.AlreadyFoundException;
import com.aswe.communicraft.exceptions.NotFoundException;
import com.aswe.communicraft.models.dto.ProjectDto;
import com.aswe.communicraft.models.dto.TaskDto;
import jakarta.servlet.http.HttpServletRequest;

public interface ProjectService {
    void addProject(ProjectDto projectDto) throws AlreadyFoundException;
    ProjectDto findByName(String name) throws NotFoundException;
    void joinProject(String name, HttpServletRequest request) throws NotFoundException;


}