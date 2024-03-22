package com.aswe.communicraft.services;

import com.aswe.communicraft.exceptions.UserAlreadyFoundException;
import com.aswe.communicraft.models.dto.LoginDto;
import com.aswe.communicraft.models.dto.RegisterDto;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    void register(RegisterDto registerDto) throws UserAlreadyFoundException;
    String login (LoginDto loginDto, HttpServletResponse response);
}
