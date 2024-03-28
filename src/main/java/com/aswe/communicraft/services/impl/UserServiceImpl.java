package com.aswe.communicraft.services.impl;

import com.aswe.communicraft.annotations.HidePasswordIfNotAdmin;
import com.aswe.communicraft.exceptions.NotFoundException;
import com.aswe.communicraft.mapper.Mapper;
import com.aswe.communicraft.models.dto.UserDto;
import com.aswe.communicraft.models.entities.CraftEntity;
import com.aswe.communicraft.models.entities.UserEntity;
import com.aswe.communicraft.repositories.CraftRepository;
import com.aswe.communicraft.repositories.UserRepository;
import com.aswe.communicraft.security.SecurityConfig;
import com.aswe.communicraft.services.UserService;
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

    @Override
    public void update(UserDto userDto, int id) throws NotFoundException {
        Optional<UserEntity> userOptional = userRepository.findById(id);
        CraftEntity craftEntity = craftRepository.findByName(userDto.getCraft().getName());

        if(userOptional.isEmpty()) {
            LOGGER.error("User with id = " + id + " Not exist!");
            throw new NotFoundException("User with id = " + id + " Not exist!");
        }

        if(craftEntity == null){
            craftEntity = new CraftEntity();
            craftEntity.setName(userDto.getCraft().getName());
            craftRepository.save(craftEntity);
        }



        userOptional.get().setUserName(userDto.getUserName());
        userOptional.get().setEmail(userDto.getEmail());
        userOptional.get().setPassword(securityConfig.passwordEncoder().encode(userDto.getPassword()));
        userOptional.get().setLevelOfSkill(userDto.getLevelOfSkill());
        userOptional.get().setCraftEntity(craftEntity);

        userRepository.save(userOptional.get());
    }

    @Override
    public List<UserDto> findAllUsers() throws NotFoundException {
        List<UserEntity> users = userRepository.findAll();

        if(users.isEmpty()) {
            LOGGER.error("No any user exist in the system!");
            throw new NotFoundException("No any user in users table!");
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
            LOGGER.error("This user with id = " + id + " not exist!");
            throw new NotFoundException("This user with id = " + id + " not exist!");
        }

        return mapper.toDto(userEntity,UserDto.class);
    }
    @HidePasswordIfNotAdmin
    @Override
    public UserDto findByUsername(String name) throws NotFoundException {
        Optional<UserEntity> user = userRepository.findByUserName(name);
        System.out.println(user.get().getUserName());

        if (user.isEmpty())
            throw new NotFoundException("User not exist with name: " + name);

        return mapper.toDto(user.get(), UserDto.class);
    }

    @Override
    @HidePasswordIfNotAdmin
    public List<UserDto> findUsersByCraft(String craft) throws NotFoundException {
        CraftEntity craftEntity = craftRepository.findByName(craft);
        List<UserEntity> users = userRepository.findByCraftEntity(craftEntity);

        if(users.isEmpty()) {
            LOGGER.error("No any user exist in the system!");
            throw new NotFoundException("No any user in users table!");
        }

        if(craftEntity == null){
            LOGGER.error("No any craft with this name exist in the system!");
            throw new NotFoundException("No any craft in craft table!");
        }

        return users.stream()
                .filter(user -> !user.isDeleted())
                .map(user -> mapper.toDto(user, UserDto.class)).toList();
    }


    @Override
    public void deleteUser(int id) throws NotFoundException {
        UserEntity userEntity = userRepository.findById(id).orElse(null);

        if(userEntity == null) {
            LOGGER.error("This user with id = " + id + " not exist!");
            throw new NotFoundException("This user with id = " + id + " not exist!");
        }


        userRepository.softDeleteById(id);
    }






}
