package com.aswe.communicraft.services;

import com.aswe.communicraft.exceptions.AlreadyExistsException;
import com.aswe.communicraft.models.dto.CraftDto;

public interface CraftService {
    void addCraft(CraftDto craftDto) throws AlreadyExistsException;
}
