package com.aswe.communicraft.services;

import com.aswe.communicraft.exceptions.AlreadyExistsException;
import com.aswe.communicraft.exceptions.NotFoundException;
import com.aswe.communicraft.models.dto.ProjectDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Optional;

public interface ProjectService {
    void addProject(ProjectDto projectDto) throws AlreadyExistsException;
    ProjectDto findByName(String name) throws NotFoundException;
    void joinProject(String name, HttpServletRequest request) throws NotFoundException;


    List<ProjectDto> findFinishedProject() throws NotFoundException;

    void buyAProjectByName(String projectName, HttpServletRequest request) throws NotFoundException;

    Optional<List<ProjectDto>> findAllProjects(HttpServletRequest request) throws NotFoundException;
}