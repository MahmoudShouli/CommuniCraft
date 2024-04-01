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
public class TaskController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskController.class);
    private final TaskService taskService;

    @PostMapping("/{projectName}")
    @PreAuthorize("hasAuthority('CRAFTSMAN')")
    public ResponseEntity<String> createTask(@RequestBody TaskDto taskDto, @PathVariable String projectName, HttpServletRequest request) throws NotFoundException {
        taskService.createTask(taskDto, projectName, request);

        return ResponseEntity.ok().body("Task Created Successfully!");
    }



    @PostMapping("/assign")
    @PreAuthorize("hasAuthority('CRAFTSMAN')")
    public ResponseEntity<String> assignTask(@RequestBody AssignTaskDto assignTaskDto, HttpServletRequest request) throws NotFoundException {
        taskService.assignTask(assignTaskDto, request);
        LOGGER.info("assigning task: " + assignTaskDto.getTaskName() + "to user: " + assignTaskDto.getUserName());
        return ResponseEntity.ok().body("Task Assigned Successfully!");
    }

    @PostMapping("finish")
    @PreAuthorize("hasAuthority('CRAFTSMAN')")
    public ResponseEntity<String> finishTask(HttpServletRequest request) throws NotFoundException {
        taskService.finishTask(request);
        LOGGER.info("finishing task ");
        return ResponseEntity.ok().body("Task Finished Successfully!");
    }



}
