package com.aswe.communicraft.controllers;


import com.aswe.communicraft.exceptions.AlreadyFoundException;
import com.aswe.communicraft.exceptions.NotFoundException;
import com.aswe.communicraft.models.dto.ProjectDto;
import com.aswe.communicraft.models.dto.TaskDto;
import com.aswe.communicraft.services.ProjectService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/projects")
@RequiredArgsConstructor
public class ProjectController {


    private final ProjectService projectService;

    @PostMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> addProject(@Valid @RequestBody ProjectDto projectDto) throws AlreadyFoundException {

        projectService.addProject(projectDto);

        return ResponseEntity.ok().body("Project Created Successfully!");
    }


    @GetMapping(value = "/{project_name}")
    public ResponseEntity<ProjectDto> findProject(@PathVariable String name) throws NotFoundException {

        ProjectDto projectDto = projectService.findByName(name);

        return ResponseEntity.ok(projectDto);

    }

    @PostMapping("/join/{project_name}")
    @PreAuthorize("hasAuthority('CRAFTSMAN')")
    public ResponseEntity<String> joinProject(@PathVariable String name, HttpServletRequest request) throws NotFoundException {
        projectService.joinProject(name, request);

        return ResponseEntity.ok().body("Joined Project Successfully!");
    }

}
