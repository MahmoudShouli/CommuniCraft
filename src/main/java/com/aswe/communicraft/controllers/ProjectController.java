package com.aswe.communicraft.controllers;


import com.aswe.communicraft.exceptions.AlreadyExistsException;
import com.aswe.communicraft.exceptions.NotFoundException;
import com.aswe.communicraft.models.dto.ProjectDto;
import com.aswe.communicraft.services.ProjectService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/projects")
@RequiredArgsConstructor
public class ProjectController {


    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectController.class);
    private final ProjectService projectService;

    @PostMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> addProject(@Valid @RequestBody ProjectDto projectDto) throws AlreadyExistsException {

        projectService.addProject(projectDto);
        LOGGER.info("adding project: " + projectDto.getName());
        return ResponseEntity.ok().body("Project Created Successfully!");
    }


    @GetMapping(value = "/{project_name}")
    public ResponseEntity<ProjectDto> findProject(@PathVariable String project_name) throws NotFoundException {

        ProjectDto projectDto = projectService.findByName(project_name);
        LOGGER.info("finding project: " + project_name);
        return ResponseEntity.ok(projectDto);

    }

    @PostMapping("/join/{project_name}")
    @PreAuthorize("hasAuthority('CRAFTSMAN')")
    public ResponseEntity<String> joinProject(@PathVariable String project_name, HttpServletRequest request) throws NotFoundException {
        projectService.joinProject(project_name, request);
        LOGGER.info("joining project: " + project_name);
        return ResponseEntity.ok().body("Joined Project Successfully!");
    }

}
