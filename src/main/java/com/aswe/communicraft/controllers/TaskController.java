package com.aswe.communicraft.controllers;

import com.aswe.communicraft.exceptions.NotFoundException;
import com.aswe.communicraft.models.dto.AssignTaskDto;
import com.aswe.communicraft.models.dto.TaskDto;
import com.aswe.communicraft.services.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor

/*
  The TaskController class is a REST controller that handles task-related requests.
 */
public class TaskController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskController.class);
    private final TaskService taskService;
    /**
     * Handles the request to create a task to the system.
     *
     * @param taskDto  the taskDto object containing task details
     * @param projectName  the projectName String containing the project name I want to crate the task to
     * @param request  the request containing logged-in user details
     * @return ResponseEntity with success message if the task added successfully
     */
    @PostMapping("/{projectName}")
    @PreAuthorize("hasAuthority('CRAFTSMAN')")
    public ResponseEntity<String> createTask(@RequestBody TaskDto taskDto, @PathVariable String projectName, HttpServletRequest request) throws NotFoundException {
        taskService.createTask(taskDto, projectName, request);
        LOGGER.info("creating task: {} to project: {}", taskDto.getName(), projectName);
        return ResponseEntity.ok().body("Task Created Successfully!");
    }

    /**
     * Handles the request to assign a task to a user in the system.
     *
     * @param assignTaskDto  the assignTaskDto object containing the details needed
     * @param request  the request containing logged-in user details
     * @return ResponseEntity with success message if the task assigned successfully
     */
    @PostMapping("/assign")
    @PreAuthorize("hasAuthority('CRAFTSMAN')")
    public ResponseEntity<String> assignTask(@RequestBody AssignTaskDto assignTaskDto, HttpServletRequest request) throws NotFoundException {
        taskService.assignTask(assignTaskDto, request);
        LOGGER.info("assigning task: {} to user: {}", assignTaskDto.getTaskName(), assignTaskDto.getUserName());
        return ResponseEntity.ok().body("Task Assigned Successfully!");
    }

    /**
     * Handles the request to finish from a task by the user
     *
     * @param request  the request containing logged-in user details
     * @return ResponseEntity with success message if the task finished successfully
     */
    @PostMapping("finish")
    @PreAuthorize("hasAuthority('CRAFTSMAN')")
    public ResponseEntity<String> finishTask(HttpServletRequest request) throws NotFoundException {
        taskService.finishTask(request);
        LOGGER.info("finishing task");
        return ResponseEntity.ok().body("Task Finished Successfully!");
    }
}
