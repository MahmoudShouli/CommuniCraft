package com.aswe.communicraft.controllers;


import com.aswe.communicraft.annotations.HidePasswordIfNotAdmin;
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

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/projects")
@RequiredArgsConstructor
public class ProjectController {


    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectController.class);
    private final ProjectService projectService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> addProject(@Valid @RequestBody ProjectDto projectDto) throws AlreadyExistsException {

        projectService.addProject(projectDto);
        LOGGER.info("adding project: {}", projectDto.getName());
        return ResponseEntity.ok().body("Project Created Successfully!");
    }


    @GetMapping(value = "/{projectName}")
    public ResponseEntity<ProjectDto> findProject(@PathVariable String projectName) throws NotFoundException {

        ProjectDto projectDto = projectService.findByName(projectName);
        LOGGER.info("finding project: {}", projectName);
        return ResponseEntity.ok(projectDto);
    }
    @GetMapping
    @PreAuthorize("hasAuthority('CRAFTSMAN')")
    public ResponseEntity<Optional<List<ProjectDto>>> findAllProjects(HttpServletRequest request) throws NotFoundException {
        Optional<List<ProjectDto>> projects = projectService.findAllProjects(request);
        LOGGER.info("finding all project by skill");
        return ResponseEntity.ok(projects);
    }

    @PostMapping("/join/{projectName}")
    @PreAuthorize("hasAuthority('CRAFTSMAN')")
    public ResponseEntity<String> joinProject(@PathVariable String projectName, HttpServletRequest request) throws NotFoundException {
        projectService.joinProject(projectName, request);
        LOGGER.info("joining project: {}", projectName);
        return ResponseEntity.ok().body("Joined Project Successfully!");
    }

    @GetMapping("/finished")
    @PreAuthorize("hasAuthority('BUYER')")
    @HidePasswordIfNotAdmin
    public ResponseEntity<List<ProjectDto>> findAllFinishedProject() throws NotFoundException {
        List<ProjectDto> finishedProjects = projectService.findFinishedProject();
        LOGGER.info("finding all finished project");
        return ResponseEntity.ok(finishedProjects);
    }

    @PostMapping ("/finished/buy/{projectName}")
    @PreAuthorize("hasAuthority('BUYER')")
    public ResponseEntity<String> buyAProjectByName(@PathVariable String projectName , HttpServletRequest request) throws NotFoundException {
        projectService.buyAProjectByName(projectName , request);
        LOGGER.info("adding project with name: {}", projectName);
        return ResponseEntity.ok("The project bought Successfully!");

    }



}
