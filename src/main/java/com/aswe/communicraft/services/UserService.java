package com.aswe.communicraft.services;

import com.aswe.communicraft.exceptions.NotFoundException;
import com.aswe.communicraft.models.dto.UserDto;

public interface UserService {
    public void update(UserDto userDto , int id) throws NotFoundException;
    UserDto findById(int id) throws NotFoundException;
}
