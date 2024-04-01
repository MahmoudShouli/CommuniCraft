package com.aswe.communicraft.services.impl;

import com.aswe.communicraft.exceptions.AlreadyExistsException;
import com.aswe.communicraft.exceptions.NotFoundException;
import com.aswe.communicraft.mapper.Mapper;
import com.aswe.communicraft.models.dto.ProjectDto;
import com.aswe.communicraft.models.entities.*;
import com.aswe.communicraft.models.enums.Role;
import com.aswe.communicraft.models.enums.Skill;
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
    private static final String AUTHORIZATION = "AUTHORIZATION";

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
        projectEntity.setProjectSkill(projectDto.getProjectSkill());
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
        String token = request.getHeader(AUTHORIZATION);
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
                .orElseThrow(() -> new NotFoundException("User not found"));

        String craftName = user.getCraft().getName();

        Skill skill = user.getLevelOfSkill();

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

        if(skill != project.getProjectSkill()){
            LOGGER.error("This project is not matching your Skills :( .");
            throw new NotFoundException("This project is not matching your Skills :( ");
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

    @Override
    public List<ProjectDto> findFinishedProject() throws NotFoundException {
        Optional<List<ProjectEntity>> finishedProjects = projectRepository.findAllFinishedProjects();

        if (finishedProjects.isEmpty()){
            throw new NotFoundException("There is no finished projects yet");
        }



        return getProjects(finishedProjects);

    }

    @Override
    public void buyAProjectByName(String projectName, HttpServletRequest request) throws NotFoundException {
        String token = request.getHeader(AUTHORIZATION);
        int id = jwtUtils.getIdFromJwtToken(token);

        ProjectEntity project = projectRepository.findByName(projectName)
                .orElseThrow(() -> new NotFoundException("Project not found with name: " + projectName));

        Optional<UserEntity> user = userRepository.findById(id);

        if (user.isEmpty()){
            throw new NotFoundException("User not found with id: " + id);
        }

        user.get().setProject(project);
        userRepository.save(user.get());

        Optional<List<UserEntity>> admins = userRepository.findByRole(Role.ADMIN);
        List<String> emails = new ArrayList<>();
        String content;

        if (admins.isEmpty()){
            throw new NotFoundException("no admins in the system");
        }

        for (int i=0 ; i<admins.get().size();i++){
            emails.add(admins.get().get(i).getEmail());
            content = "Hello Admin: " + admins.get().get(i).getUserName() + "!\n" +  user.get().getUserName() + " has bought your project :" +
                       project.getName();

            emailService.sendEmail(emails.get(i),"CommuniCraft Email Notification",content);
        }

    }

    @Override
    public Optional<List<ProjectDto>> findAllProjects(HttpServletRequest request) throws NotFoundException {
        String token = request.getHeader(AUTHORIZATION);
        int id = jwtUtils.getIdFromJwtToken(token);

        Optional<UserEntity> user = userRepository.findById(id);
        if (user.isEmpty()){
            throw new NotFoundException("User not found with id: " + id);
        }

        Skill skill = user.get().getLevelOfSkill();

        Optional<List<ProjectEntity>> projects = projectRepository.findAllProjectsBySkill(skill);

        if (projects.isEmpty()){
            throw new NotFoundException("No projects with this skill in the system");
        }

        List<ProjectDto> projectsDto = getProjects(projects);


        return Optional.of(projectsDto);
    }

    private List<ProjectDto> getProjects(Optional<List<ProjectEntity>> projects) throws NotFoundException {
        if (projects.isEmpty()){
            throw new NotFoundException("no Projects found");
        }
        List<ProjectDto> projectsDto = projects.get().stream()
                .map(project -> projectMapper.toDto(project, ProjectDto.class)).toList();

        for (int i=0 ; i<projects.get().size();i++){
            ProjectEntity project = projects.get().get(i);
            projectsDto.get(i).setCraftsNeeded(project.getProjectCrafts().stream().map(projectCraft -> projectCraft.getCraft().getName()).toList());
        }
        return projectsDto;
    }

}
