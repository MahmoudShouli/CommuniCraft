package com.aswe.communicraft.services.impl;

import com.aswe.communicraft.annotations.HidePasswordIfNotAdmin;
import com.aswe.communicraft.exceptions.NotFoundException;
import com.aswe.communicraft.mapper.Mapper;
import com.aswe.communicraft.models.dto.ProjectLeaderDto;
import com.aswe.communicraft.models.dto.UserDto;
import com.aswe.communicraft.models.entities.CraftEntity;
import com.aswe.communicraft.models.entities.ProjectEntity;
import com.aswe.communicraft.models.entities.UserEntity;
import com.aswe.communicraft.models.enums.Role;
import com.aswe.communicraft.repositories.CraftRepository;
import com.aswe.communicraft.repositories.ProjectRepository;
import com.aswe.communicraft.repositories.UserRepository;
import com.aswe.communicraft.security.JwtUtils;
import com.aswe.communicraft.security.SecurityConfig;
import com.aswe.communicraft.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private final SecurityConfig securityConfig;
    private final Mapper<UserEntity, UserDto> mapper;
    private final UserRepository userRepository;
    private final CraftRepository craftRepository;
    private final ProjectRepository projectRepository;
    private final JwtUtils jwtUtils;

    @Override
    public void update(UserDto userDto, HttpServletRequest request) throws NotFoundException {
        String token = request.getHeader("Authorization");
        int id = jwtUtils.getIdFromJwtToken(token);

        Optional<UserEntity> userOptional = userRepository.findById(id);

        if(userOptional.isEmpty()) {
            LOGGER.error("This user with id = {} not exist ", id);
            throw new NotFoundException("User with id = " + id + " Not exist!");
        }

        if(userOptional.get().getRole() == Role.CRAFTSMAN){
            userOptional.get().setLevelOfSkill(userDto.getLevelOfSkill());

            Optional<CraftEntity> existingCraft = craftRepository.findByName(userDto.getCraft().getName());
            if(existingCraft.isEmpty()) {
                CraftEntity newCraft = new CraftEntity();
                newCraft.setName(userDto.getCraft().getName());
                userOptional.get().setCraft(newCraft);
                craftRepository.save(newCraft);
            }
            else {
                userOptional.get().setCraft(existingCraft.get());
            }
        }

        userOptional.get().setUserName(userDto.getUserName());
        userOptional.get().setEmail(userDto.getEmail());
        userOptional.get().setPassword(securityConfig.passwordEncoder().encode(userDto.getPassword()));


        userRepository.save(userOptional.get());
    }

    @Override
    public List<UserDto> findAllUsers() throws NotFoundException {
        List<UserEntity> users = userRepository.findAll();

        if(users.size()==1) {
            LOGGER.error("No users are in the system at the moment.");
            throw new NotFoundException("No users are in the system at the moment.");
        }

        return users.stream()
                .filter(user -> !user.isDeleted())
                .map(user -> mapper.toDto(user, UserDto.class)).toList();
    }

    @HidePasswordIfNotAdmin
    @Override
    public UserDto findById(int id) throws NotFoundException {
        UserEntity userEntity = userRepository.findById(id).orElse(null);

        if(userEntity == null) {
            LOGGER.error("This user with id = {} not exist!", id);
            throw new NotFoundException("This user with id = " + id + " not exist!");
        }

        return mapper.toDto(userEntity,UserDto.class);
    }
    @HidePasswordIfNotAdmin
    @Override
    public UserDto findByUsername(String name) throws NotFoundException {
        Optional<UserEntity> user = userRepository.findByUserName(name);


        if (user.isEmpty())
            throw new NotFoundException("User not exist with name: " + name);

        return mapper.toDto(user.get(), UserDto.class);
    }

    @Override
    @HidePasswordIfNotAdmin
    public List<UserDto> findUsersByCraft(String craft) throws NotFoundException {
        Optional<CraftEntity> craftEntity = craftRepository.findByName(craft);


        if(craftEntity.isEmpty()){
            LOGGER.error("No any craft with this name exist in the system!");
            throw new NotFoundException("No any craft in craft table!");
        }

        List<UserEntity> users = userRepository.findByCraft(craftEntity.get());

        if(users.isEmpty()) {
            LOGGER.error("No any user exist in the system!");
            throw new NotFoundException("No any user in users table!");
        }

        return users.stream()
                .filter(user -> !user.isDeleted())
                .map(user -> mapper.toDto(user, UserDto.class)).toList();
    }

    @Override
    public void makeLeader(ProjectLeaderDto projectLeaderDto) throws NotFoundException {
        String userName = projectLeaderDto.getUserName();
        String projectName = projectLeaderDto.getProjectName();

        Optional<UserEntity> user = userRepository.findByUserName(userName);

        if (user.isEmpty() || user.get().isDeleted()){
            throw new NotFoundException("User not exist with name: " + userName);
        }

        ProjectEntity project = projectRepository.findByName(projectName)
                .orElseThrow(() -> new NotFoundException("Project not found with name: " + projectName));

        user.get().setProject(project);
        project.getCraftsmenList().add(user.get());


        projectRepository.save(project);
        userRepository.makeLeader(userName);
    }


    @Override
    public void deleteUser(int id) throws NotFoundException {
        UserEntity userEntity = userRepository.findById(id).orElse(null);

        if(userEntity == null) {
            LOGGER.error("This user with id = {} not exist!", id);
            throw new NotFoundException("This user with id = " + id + " not exist!");
        }

        userRepository.softDeleteById(id);
    }


}
