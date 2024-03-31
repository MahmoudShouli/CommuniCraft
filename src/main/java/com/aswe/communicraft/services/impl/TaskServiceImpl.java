package com.aswe.communicraft.services.impl;

import com.aswe.communicraft.exceptions.NotFoundException;
import com.aswe.communicraft.mapper.Mapper;
import com.aswe.communicraft.models.dto.TaskDto;
import com.aswe.communicraft.models.entities.ProjectEntity;
import com.aswe.communicraft.models.entities.TaskEntity;
import com.aswe.communicraft.models.entities.UserEntity;
import com.aswe.communicraft.repositories.ProjectRepository;
import com.aswe.communicraft.repositories.TaskRepository;
import com.aswe.communicraft.repositories.UserRepository;
import com.aswe.communicraft.security.JwtUtils;
import com.aswe.communicraft.services.EmailService;
import com.aswe.communicraft.services.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {


    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final Mapper<TaskEntity, TaskDto> taskMapper;
    private final TaskRepository taskRepository;
    private final EmailService emailService;

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


    @Override
    public void assignTask(TaskDto taskDto, String userName, HttpServletRequest request) throws NotFoundException {
        String token = request.getHeader("Authorization");
        int id = jwtUtils.getIdFromJwtToken(token);


        // this is the leader assigning
        UserEntity LeaderUser = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));

        // this is the user being assigned to
        Optional<UserEntity> user = userRepository.findByUserName(userName);

        if (user.isEmpty()){
            throw new NotFoundException("User not found with id: " + id);
        }

        if(!LeaderUser.isLeader()) {
            throw new NotFoundException(("You must be a leader."));
        }

        if(user.get().getTask() != null){
            throw new NotFoundException("the user should finish his task first");
        }
        Optional<TaskEntity> task = taskRepository.findByName(taskDto.getName());

        if (task.isEmpty()){
            throw new NotFoundException("task not found with this name: " + taskDto.getName());
        }


        user.get().setTask(task.get());
        userRepository.save(user.get());

        String email = user.get().getEmail();
        String content = "Hello " + userName + "!\n" +  "You have been assigned to task: " + task.get().getName() + "\n"
                +  "which belongs to project " + task.get().getProject().getName();

        emailService.sendEmail(email,"CommuniCraft Email Notification",content);
    }

    @Override
    public void finishTask(String taskName, HttpServletRequest request) throws NotFoundException {
        String token = request.getHeader("Authorization");
        int id = jwtUtils.getIdFromJwtToken(token);

        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));

        Optional<TaskEntity> task = taskRepository.findByName(taskName);

        if (task.isEmpty()){
            throw new NotFoundException("Task not found with name: " + taskName);
        }

        ProjectEntity project = task.get().getProject();

        int projectID = project.getId();

        Optional<UserEntity> leader = userRepository.findByIsLeaderAndProjectId(true, projectID);

        if(leader.isEmpty() || leader.get().isDeleted()) {
            throw new NotFoundException("No leader found for this project.");
        }


        task.get().setFinished(true);
        user.setTask(null);
        project.setNumberOfTasks(project.getNumberOfTasks() -1);

        if(project.getNumberOfTasks() == 0){
            project.setFinished(true);
        }


        taskRepository.save(task.get());
        projectRepository.save(project);
        userRepository.save(user);


        String email = leader.get().getEmail();
        String content = "Hello leader: " + leader.get().getUserName() + "!\n" +  user.getUserName() + " has finished the task: " +
                task.get().getName()
                +  "\nwhich belongs to project " + task.get().getProject().getName();

        emailService.sendEmail(email,"CommuniCraft Email Notification",content);
    }

}
