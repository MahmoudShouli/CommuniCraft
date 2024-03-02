package com.aswe.communicraft.controllers;


import com.aswe.communicraft.exceptions.UserAlreadyFoundException;
import com.aswe.communicraft.models.dto.RegisterDto;
import com.aswe.communicraft.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterDto registerDto) throws UserAlreadyFoundException {

        authService.register(registerDto);

        return ResponseEntity.ok().body("User Created Successfully!");
    }
}
