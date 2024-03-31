package com.aswe.communicraft.controllers;

import com.aswe.communicraft.exceptions.NotFoundException;
import com.aswe.communicraft.models.dto.TaskDto;
import com.aswe.communicraft.services.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/{task_name}")
    @PreAuthorize("hasAuthority('CRAFTSMAN')")
    public ResponseEntity<String> createTask(@RequestBody TaskDto taskDto, @PathVariable String name, HttpServletRequest request) throws NotFoundException {
        taskService.createTask(taskDto, name, request);

        return ResponseEntity.ok().body("Task Created Successfully!");
    }



    @PostMapping("assign/{user_name}")
    @PreAuthorize("hasAuthority('CRAFTSMAN')")
    public ResponseEntity<String> assignTask(@RequestBody TaskDto taskDto, @PathVariable String name, HttpServletRequest request) throws NotFoundException {
        taskService.createTask(taskDto, name, request);

        return ResponseEntity.ok().body("Task Assigned Successfully!");
    }

}
