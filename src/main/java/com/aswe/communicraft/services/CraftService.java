package com.aswe.communicraft.services;

import com.aswe.communicraft.exceptions.AlreadyFoundException;
import com.aswe.communicraft.models.dto.CraftDto;
import com.aswe.communicraft.models.dto.ProjectDto;

public interface CraftService {
    void addCraft(CraftDto craftDto) throws AlreadyFoundException;
}
