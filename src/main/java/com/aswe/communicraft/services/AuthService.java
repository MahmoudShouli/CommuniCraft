package com.aswe.communicraft.services;

import com.aswe.communicraft.exceptions.UserAlreadyFoundException;
import com.aswe.communicraft.models.dto.RegisterDto;

public interface AuthService {
    void register(RegisterDto registerDto) throws UserAlreadyFoundException;
}
