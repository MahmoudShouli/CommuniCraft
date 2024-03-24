package com.aswe.communicraft.controllers;


import com.aswe.communicraft.exceptions.UserAlreadyFoundException;
import com.aswe.communicraft.models.dto.LoginDto;
import com.aswe.communicraft.models.dto.RegisterDto;
import com.aswe.communicraft.services.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;



@RestController
@RequestMapping(value = "/auth")
@RequiredArgsConstructor
public class AuthController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;



    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterDto registerDto) throws UserAlreadyFoundException {

        authService.register(registerDto);

        return ResponseEntity.ok().body("User Created Successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto, HttpServletResponse response) {
        String jwt = authService.login(loginDto, response);
        LOGGER.info("log in user with name: " + loginDto.getUserName());
        return ResponseEntity.ok().body("Here is your token:\n " + jwt);
    }
}
