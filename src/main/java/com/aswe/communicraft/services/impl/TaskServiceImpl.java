package com.aswe.communicraft.services.impl;

import com.aswe.communicraft.exceptions.NotFoundException;
import com.aswe.communicraft.mapper.Mapper;
import com.aswe.communicraft.models.dto.QuoteResponse;
import com.aswe.communicraft.models.dto.AssignTaskDto;
import com.aswe.communicraft.models.dto.TaskDto;
import com.aswe.communicraft.models.entities.ProjectEntity;
import com.aswe.communicraft.models.entities.TaskEntity;
import com.aswe.communicraft.models.entities.UserEntity;
import com.aswe.communicraft.repositories.ProjectRepository;
import com.aswe.communicraft.repositories.TaskRepository;
import com.aswe.communicraft.repositories.UserRepository;
import com.aswe.communicraft.security.JwtUtils;
import com.aswe.communicraft.services.EmailService;
import com.aswe.communicraft.services.ExternalAPIService;
import com.aswe.communicraft.services.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import java.util.Optional;


@Service
@RequiredArgsConstructor
/*
 * The TaskService class provides task-related services.
 */
public class TaskServiceImpl implements TaskService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskServiceImpl.class);
    private static final String AUTHORIZATION = "Authorization";
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final Mapper<TaskEntity, TaskDto> taskMapper;
    private final TaskRepository taskRepository;
    private final EmailService emailService;
    private final ExternalAPIService externalAPIService;

    @Override
    public void createTask(TaskDto taskDto, String name, HttpServletRequest request) throws NotFoundException {

        String token = request.getHeader(AUTHORIZATION);
        int id = jwtUtils.getIdFromJwtToken(token);

        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found "));

        if(!user.isLeader()) {
            LOGGER.error("You must be a leader");
            throw new NotFoundException(("You must be a leader"));
        }

        ProjectEntity project = projectRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Project not found with name: " + name));

        if (project.isFinished()){
            throw new NotFoundException("this project is already finished");
        }

        TaskEntity task = taskMapper.toEntity(taskDto, TaskEntity.class);
        project.getTasks().add(task);
        task.setProject(project);
        project.setNumberOfTasks(project.getNumberOfTasks()+1);

        taskRepository.save(task);
        projectRepository.save(project);
    }


    @Override
    public void assignTask(AssignTaskDto assignTaskDto, HttpServletRequest request) throws NotFoundException {
        String token = request.getHeader(AUTHORIZATION);
        int id = jwtUtils.getIdFromJwtToken(token);

        // this is the leader assigning
        UserEntity leaderUser = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        // this is the user being assigned to
        Optional<UserEntity> user = userRepository.findByUserName(assignTaskDto.getUserName());

        if (user.isEmpty()){
            LOGGER.error("User not found with name: {}", assignTaskDto.getUserName());
            throw new NotFoundException("User not found with id: " + assignTaskDto.getUserName());
        }

        if(!leaderUser.isLeader()) {
            LOGGER.error("You must be a leader.");
            throw new NotFoundException(("You must be a leader."));
        }

        if(user.get().getTask() != null){
            LOGGER.error("the user should finish his task first");
            throw new NotFoundException("the user should finish his task first");
        }
        Optional<TaskEntity> task = taskRepository.findByName(assignTaskDto.getTaskName());

        if (task.isEmpty()){
            LOGGER.error("task not found with this name: {}", assignTaskDto.getTaskName());
            throw new NotFoundException("task not found with this name: " + assignTaskDto.getTaskName());
        }

        if (task.get().isFinished()){
            throw new NotFoundException("this task is already finished");
        }
        QuoteResponse quote = externalAPIService.getRandomQuote();
        user.get().setTask(task.get());
        userRepository.save(user.get());
        String email = user.get().getEmail();
        String content = "Hello " + user.get().getUserName() + "!\n" +  "You have been assigned to task: " + task.get().getName() + "\n"
                +  "which belongs to project " + task.get().getProject().getName() +
                 "\n here is a quote to motivate you" + "☺ : \n" + "\""+quote.getQ() +"\""+ " said by : " + quote.getA();

        emailService.sendEmail(email,"CommuniCraft Email Notification",content);
    }

    @Override
    public void finishTask(HttpServletRequest request) throws NotFoundException {
        String token = request.getHeader(AUTHORIZATION);
        int id = jwtUtils.getIdFromJwtToken(token);

        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));

        Optional<TaskEntity> task = Optional.of(user.getTask());

        ProjectEntity project = task.get().getProject();

        int projectID = project.getId();

        Optional<UserEntity> leader = userRepository.findByIsLeaderAndProjectId(true, projectID);

        if(leader.isEmpty() || leader.get().isDeleted()) {
            LOGGER.error("No leader found for this project.");
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
