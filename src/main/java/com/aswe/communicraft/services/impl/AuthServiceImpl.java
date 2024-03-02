package com.aswe.communicraft.services.impl;

import com.aswe.communicraft.exceptions.UserAlreadyFoundException;
import com.aswe.communicraft.mapper.Mapper;
import com.aswe.communicraft.models.dto.RegisterDto;
import com.aswe.communicraft.models.entities.CraftEntity;
import com.aswe.communicraft.models.entities.RoleEntity;
import com.aswe.communicraft.models.entities.UserEntity;
import com.aswe.communicraft.repositories.CraftRepo;
import com.aswe.communicraft.repositories.UserRepo;
import com.aswe.communicraft.security.SecurityConfig;
import com.aswe.communicraft.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {


    private final UserRepo userRepo;
    private final CraftRepo craftRepo;
    private final Mapper<UserEntity , RegisterDto> mapper;
    private final SecurityConfig securityConfig;


    /**
     * Register new user and save him in the DB
     * all users at init. take the role (user)
     * the password encoded
     * @param registerDto object has all the user registration details
     */

    @Override
    public void register(RegisterDto registerDto) throws UserAlreadyFoundException {
        Optional<UserEntity> user = userRepo.findByUserName(registerDto.getUserName());
        CraftEntity craftEntity = craftRepo.findByCraftName(registerDto.getCraft().getCraftName());


        if(craftEntity == null){
            craftEntity = new CraftEntity();
            craftEntity.setCraftName(registerDto.getCraft().getCraftName());
            craftRepo.save(craftEntity);
        }

        if (user.isPresent()){
            throw new UserAlreadyFoundException("user already exists");
        }

        UserEntity userEntity = mapper.toEntity(registerDto , UserEntity.class);
        userEntity.setCraft(craftEntity);
        userEntity.setRoleEntity(new RoleEntity(1,"user")); // all new registered set to role user
        userEntity.setPassword(securityConfig.passwordEncoder().encode(userEntity.getPassword()));

        userRepo.save(userEntity);
    }
}
