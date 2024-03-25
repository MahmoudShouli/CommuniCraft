package com.aswe.communicraft.services.impl;

import com.aswe.communicraft.exceptions.AlreadyFoundException;
import com.aswe.communicraft.exceptions.NotFoundException;
import com.aswe.communicraft.mapper.Mapper;
import com.aswe.communicraft.models.dto.ProjectDto;
import com.aswe.communicraft.models.entities.ProjectEntity;
import com.aswe.communicraft.repositories.ProjectRepository;
import com.aswe.communicraft.services.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final Mapper<ProjectEntity, ProjectDto> mapper;


    // @param projectDto object has all the user registration details
    @Override
    public void addProject(ProjectDto projectDto) throws AlreadyFoundException {
        Optional<ProjectEntity> project= projectRepository.findByName(projectDto.getName());

        if (project.isPresent())
            throw new AlreadyFoundException("Name already exists. (copyrights issues)");

       ProjectEntity projectEntity = mapper.toEntity(projectDto, ProjectEntity.class);

       projectRepository.save(projectEntity);
    }

    @Override
    public ProjectDto findByName(String name) throws NotFoundException {
        Optional<ProjectEntity> project= projectRepository.findByName(name);

        if (project.isEmpty())
            throw new NotFoundException("Project Not Found");

        return mapper.toDto(project.get(), ProjectDto.class);
    }
}
