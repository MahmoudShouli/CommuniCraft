package com.aswe.communicraft.services.impl;

import com.aswe.communicraft.exceptions.LoginFailedException;
import com.aswe.communicraft.exceptions.AlreadyFoundException;
import com.aswe.communicraft.mapper.Mapper;
import com.aswe.communicraft.models.dto.LoginDto;
import com.aswe.communicraft.models.dto.RegisterDto;
import com.aswe.communicraft.models.entities.CraftEntity;
import com.aswe.communicraft.models.entities.UserEntity;
import com.aswe.communicraft.models.enums.Role;
import com.aswe.communicraft.repositories.CraftRepository;
import com.aswe.communicraft.repositories.UserRepository;
import com.aswe.communicraft.security.JwtUtils;
import com.aswe.communicraft.security.SecurityConfig;
import com.aswe.communicraft.security.UserDetailsImpl;
import com.aswe.communicraft.services.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.Optional;
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {


    private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final UserRepository userRepository;
    private final CraftRepository craftRepository;
    private final Mapper<UserEntity, RegisterDto> mapper;
    private final SecurityConfig securityConfig;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;


    /**
     * Register new user and save him in the DB
     * all users at init. take the role (user)
     * the password encoded
     *
     * @param registerDto object has all the user registration details
     */

    @Override
    public void register(RegisterDto registerDto) throws AlreadyFoundException {
        Optional<UserEntity> user = userRepository.findByUserName(registerDto.getUserName());

        if (user.isPresent()) {
            throw new AlreadyFoundException("user already exists");
        }

        UserEntity userEntity = mapper.toEntity(registerDto, UserEntity.class);
        if(registerDto.getRole() == Role.CRAFTSMAN){
            Optional<CraftEntity> existingCraft = craftRepository.findByName(registerDto.getCraft().getName());
            if(existingCraft.isEmpty()) {
                CraftEntity newCraft = new CraftEntity();
                newCraft.setName(registerDto.getCraft().getName());
                userEntity.setCraft(newCraft);
                craftRepository.save(newCraft);
            }
            else {
                userEntity.setCraft(existingCraft.get());
            }
        }

        userEntity.setPassword(securityConfig.passwordEncoder().encode(userEntity.getPassword()));

        userRepository.save(userEntity);
    }

    /**
     * Authenticates a user and generates a JWT token.
     *
     * @param loginDto the LoginDto object containing login credentials
     * @param response the HttpServletResponse object to set the response
     * @return the generated JWT token
     */
    public String login(LoginDto loginDto, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUserName(), loginDto.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            String jwt = jwtUtils.generateTokenFromUserDetails(userDetails);
            response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);
            LOGGER.info("Check user credentials and set user authentication.");
            return jwt;
        } catch (Exception e) {
            LOGGER.error("Something went wrong when login user!");
            throw new LoginFailedException(e.getMessage());
        }
    }
}
