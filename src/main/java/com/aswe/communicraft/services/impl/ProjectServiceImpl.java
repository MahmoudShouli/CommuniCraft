package com.aswe.communicraft.services.impl;

import com.aswe.communicraft.exceptions.AlreadyFoundException;
import com.aswe.communicraft.exceptions.NotFoundException;
import com.aswe.communicraft.mapper.Mapper;
import com.aswe.communicraft.models.dto.CraftDto;
import com.aswe.communicraft.models.dto.ProjectDto;
import com.aswe.communicraft.models.entities.CraftEntity;
import com.aswe.communicraft.models.entities.ProjectEntity;
import com.aswe.communicraft.models.entities.UserEntity;
import com.aswe.communicraft.repositories.CraftRepository;
import com.aswe.communicraft.repositories.ProjectRepository;
import com.aswe.communicraft.repositories.UserRepository;
import com.aswe.communicraft.security.JwtUtils;
import com.aswe.communicraft.services.ProjectService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final CraftRepository craftRepository;
    private final Mapper<ProjectEntity, ProjectDto> mapper;


    // @param projectDto object has all the user registration details
    @Override
    public void addProject(ProjectDto projectDto) throws AlreadyFoundException {
        Optional<ProjectEntity> project= projectRepository.findByName(projectDto.getName());

        if (project.isPresent())
            throw new AlreadyFoundException("Name already exists. (copyrights issues)");

        ProjectEntity projectEntity = mapper.toEntity(projectDto, ProjectEntity.class);

        projectRepository.save(projectEntity);
    }

    @Override
    public ProjectDto findByName(String name) throws NotFoundException {
        Optional<ProjectEntity> project= projectRepository.findByName(name);

        if (project.isEmpty())
            throw new NotFoundException("Project Not Found");

        return mapper.toDto(project.get(), ProjectDto.class);
    }


    // not working yet
    @Override
    public String addCraftsman(String name, HttpServletRequest request, ProjectDto projectDto) throws NotFoundException {

        String token = request.getHeader("Authorization");


        String craftName = jwtUtils.getCraftFromJwtToken(token);

        Optional<CraftEntity> craft = craftRepository.findByName(craftName);

        String userName = jwtUtils.getUserNameFromJwtToken(token);


        Optional<UserEntity> optionalUserEntity = userRepository.findByUserName(userName);
        UserEntity currentUser = optionalUserEntity.orElseThrow();
        Optional<ProjectEntity> projectOptional = projectRepository.findByName(name);



        if(projectOptional.isEmpty()) {
            throw new NotFoundException("Project with name = " + name + " not exist!");
        }





        if (!projectDto.getCraftsNeeded().contains(craft)) {
            return "This project doesn't require your craft.";

        } else if (projectDto.getTeamSize()==0) {
            return "This project is full.";

        } else {

            projectDto.getCraftsNeeded().remove(craft);
            projectDto.getCraftsmenList().add(currentUser);

            projectOptional.get().setName(projectDto.getName());
            projectOptional.get().setFinished(false);
            projectOptional.get().setTeamSize(projectDto.getTeamSize()-1);
            projectOptional.get().setNumberOfTasks(projectDto.getNumberOfTasks());
            projectOptional.get().setCraftsmenList(projectDto.getCraftsmenList());
            projectOptional.get().setCraftsNeeded(projectDto.getCraftsNeeded());


            projectRepository.save(projectOptional.get());

            return "Craftsman added to the project successfully";


        }



    }
}
