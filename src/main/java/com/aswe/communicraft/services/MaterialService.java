package com.aswe.communicraft.services;

import com.aswe.communicraft.exceptions.NotFoundException;
import com.aswe.communicraft.models.dto.MaterialDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface MaterialService {
    void addMaterial(MaterialDto materialDto, HttpServletRequest request) throws NotFoundException;

    List<MaterialDto> listAvailableMaterials() throws NotFoundException;

    void rentMaterial(int materialID) throws NotFoundException;

    void finishFromMaterial(int materialID) throws NotFoundException;
}
