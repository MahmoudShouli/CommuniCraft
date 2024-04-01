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
/*
  The ProjectController class is a REST controller that handles project-related requests.
 */
public class ProjectController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectController.class);
    private final ProjectService projectService;

    /**
     * Handles the request to add a project to the system.
     *
     * @param projectDto  the projectDto object containing project details
     * @return ResponseEntity with success message if the project added successfully
     */

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> addProject(@Valid @RequestBody ProjectDto projectDto) throws AlreadyExistsException {

        projectService.addProject(projectDto);
        LOGGER.info("adding project: {}", projectDto.getName());
        return ResponseEntity.ok().body("Project Created Successfully!");
    }

    /**
     * Handles the request to find a project by its unique name in the system.
     *
     * @param projectName  the projectName String containing project name
     * @return ResponseEntity that contain the project object
     */


    @GetMapping(value = "/{projectName}")
    public ResponseEntity<ProjectDto> findProject(@PathVariable String projectName) throws NotFoundException {

        ProjectDto projectDto = projectService.findByName(projectName);
        LOGGER.info("finding project: {}", projectName);
        return ResponseEntity.ok(projectDto);
    }

    /**
     * Handles the request to find all projects to the system.
     *
     * @param request  the request to check the Role by the token
     * @return ResponseEntity that's contain a list of all the project in the system
     */

    @GetMapping
    @PreAuthorize("hasAuthority('CRAFTSMAN')")
    public ResponseEntity<Optional<List<ProjectDto>>> findAllProjects(HttpServletRequest request) throws NotFoundException {
        Optional<List<ProjectDto>> projects = projectService.findAllProjects(request);
        LOGGER.info("finding all project by skill");
        return ResponseEntity.ok(projects);
    }

    /**
     * Handles the request to make the user join a project
     *
     * @param projectName  the project name to know which project the user wants to join
     * @param request  the request to check the Role by the token
     * @return ResponseEntity with success message if the project joined successfully
     */

    @PostMapping("/join/{projectName}")
    @PreAuthorize("hasAuthority('CRAFTSMAN')")
    public ResponseEntity<String> joinProject(@PathVariable String projectName, HttpServletRequest request) throws NotFoundException {
        projectService.joinProject(projectName, request);
        LOGGER.info("joining project: {}", projectName);
        return ResponseEntity.ok().body("Joined Project Successfully!");
    }

    /**
     * Handles the request to list all finished projects in the system
     *
     * @return ResponseEntity containing a list of finished projects
     */

    @GetMapping("/finished")
    @PreAuthorize("hasAuthority('BUYER')")
    @HidePasswordIfNotAdmin
    public ResponseEntity<List<ProjectDto>> findAllFinishedProject() throws NotFoundException {
        List<ProjectDto> finishedProjects = projectService.findFinishedProject();
        LOGGER.info("finding all finished project");
        return ResponseEntity.ok(finishedProjects);
    }

    /**
     * Handles the request to make the user join a project
     *
     * @param projectName  the project name to know which project the user wants to buy
     * @param request  the request to take the logged-in user and link the project with him
     * @return ResponseEntity with success message if the project joined successfully
     */

    @PostMapping ("/finished/buy/{projectName}")
    @PreAuthorize("hasAuthority('BUYER')")
    public ResponseEntity<String> buyAProjectByName(@PathVariable String projectName , HttpServletRequest request) throws NotFoundException {
        projectService.buyAProjectByName(projectName , request);
        LOGGER.info("adding project with name: {}", projectName);
        return ResponseEntity.ok("The project bought Successfully!");

    }
}
