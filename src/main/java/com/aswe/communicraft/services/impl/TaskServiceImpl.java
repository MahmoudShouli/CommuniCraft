package com.aswe.communicraft.services.impl;

import com.aswe.communicraft.exceptions.NotFoundException;
import com.aswe.communicraft.mapper.Mapper;
import com.aswe.communicraft.models.dto.ProjectDto;
import com.aswe.communicraft.models.dto.TaskDto;
import com.aswe.communicraft.models.entities.ProjectEntity;
import com.aswe.communicraft.models.entities.TaskEntity;
import com.aswe.communicraft.models.entities.UserEntity;
import com.aswe.communicraft.repositories.CraftRepository;
import com.aswe.communicraft.repositories.ProjectRepository;
import com.aswe.communicraft.repositories.TaskRepository;
import com.aswe.communicraft.repositories.UserRepository;
import com.aswe.communicraft.security.JwtUtils;
import com.aswe.communicraft.services.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {


    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final Mapper<TaskEntity, TaskDto> taskMapper;
    private final TaskRepository taskRepository;

    @Override
    public void createTask(TaskDto taskDto, String name, HttpServletRequest request) throws NotFoundException {

        String token = request.getHeader("Authorization");
        int id = jwtUtils.getIdFromJwtToken(token);

        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));

        if(!user.isLeader()) {
            throw new NotFoundException(("You must be a leader."));
        }


        ProjectEntity project = projectRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Project not found with name: " + name));



        TaskEntity task = taskMapper.toEntity(taskDto, TaskEntity.class);


        project.getTasks().add(task);
        task.setProject(project);
        project.setNumberOfTasks(project.getNumberOfTasks()+1);

        taskRepository.save(task);
        projectRepository.save(project);


    }

}
