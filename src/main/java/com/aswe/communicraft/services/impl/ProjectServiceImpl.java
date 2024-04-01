package com.aswe.communicraft.services.impl;

import com.aswe.communicraft.exceptions.AlreadyExistsException;
import com.aswe.communicraft.exceptions.NotFoundException;
import com.aswe.communicraft.mapper.Mapper;
import com.aswe.communicraft.models.dto.ProjectDto;
import com.aswe.communicraft.models.entities.*;
import com.aswe.communicraft.repositories.CraftRepository;
import com.aswe.communicraft.repositories.ProjectRepository;
import com.aswe.communicraft.repositories.UserRepository;
import com.aswe.communicraft.security.JwtUtils;
import com.aswe.communicraft.services.EmailService;
import com.aswe.communicraft.services.ProjectService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {


    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectServiceImpl.class);

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final CraftRepository craftRepository;
    private final Mapper<ProjectEntity, ProjectDto> projectMapper;
    private final EmailService emailService;



    @Override
    public void addProject(ProjectDto projectDto) throws AlreadyExistsException {
        Optional<ProjectEntity> project = projectRepository.findByName(projectDto.getName());


        if (project.isPresent()) {
            LOGGER.error("name already exists.");
            throw new AlreadyExistsException("Name already exists. (copyrights issues)");
        }
        ProjectEntity projectEntity = projectMapper.toEntity(projectDto, ProjectEntity.class);

        List<ProjectCraft> projectCrafts = new ArrayList<>();

        projectDto.getCraftsNeeded().forEach(craft -> {
            CraftEntity craftEntity = craftRepository.findByName(craft).orElseThrow();

            ProjectCraft projectCraft = new ProjectCraft();
            projectCraft.setCraft(craftEntity);
            projectCraft.setProject(projectEntity);

            projectCrafts.add(projectCraft);
        });

        projectEntity.setProjectCrafts(projectCrafts);
        projectRepository.save(projectEntity);
    }

    @Override
    public ProjectDto findByName(String name) throws NotFoundException {
        Optional<ProjectEntity> project = projectRepository.findByName(name);

        if (project.isEmpty()) {
            LOGGER.error("Project Not Found");
            throw new NotFoundException("Project Not Found");
        }
        ProjectDto projectDto = projectMapper.toDto(project.get(), ProjectDto.class);
        projectDto.setCraftsNeeded(
                project.get().getProjectCrafts().stream().map(projectCraft -> projectCraft.getCraft().getName()).toList()
        );

        return projectDto;
    }

    @Override
    public void joinProject(String name, HttpServletRequest request) throws NotFoundException {
        String token = request.getHeader("Authorization");
        int id = jwtUtils.getIdFromJwtToken(token);

        ProjectEntity project = projectRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Project not found with name: " + name));

        int projectID = project.getId();

        Optional<UserEntity> leader = userRepository.findByIsLeaderAndProjectId(true, projectID);

        if(leader.isEmpty() || leader.get().isDeleted()) {
            LOGGER.error("No leader found for this project.");
            throw new NotFoundException("No leader found for this project.");
        }



        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));

        String craftName = user.getCraft().getName();

        if (project.getProjectCrafts().stream().noneMatch(pc -> pc.getCraft().getName().equals(craftName))) {
            LOGGER.error("This project doesn't require your craft.");
            throw new NotFoundException("This project doesn't require your craft.");
        }
        if (project.getAvailableTeamPositions() == 0) {
            LOGGER.error("This project is full.");
            throw new NotFoundException("This project is full.");
        }
        if(user.getProject() != null) {
            LOGGER.error("User is already in a project");
            throw new NotFoundException("User is already in a project");
        }
        if(project.isFinished()) {
            LOGGER.error("This project is finished.");
            throw new NotFoundException("This project is finished.");
        }




        user.setProject(project);
        project.getCraftsmenList().add(user);
        project.setAvailableTeamPositions(project.getAvailableTeamPositions() - 1);

        userRepository.save(user);
        projectRepository.save(project);

        String email = leader.get().getEmail();
        String content = "Hello leader: " + leader.get().getUserName() + "!\n" +  user.getUserName() + " has joined your project : " +
                project.getName() + "\n you have " + project.getAvailableTeamPositions() + " positions left in your project.";


        emailService.sendEmail(email,"CommuniCraft Email Notification",content);



    }



}
