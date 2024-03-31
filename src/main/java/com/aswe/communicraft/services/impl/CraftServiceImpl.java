package com.aswe.communicraft.services.impl;


import com.aswe.communicraft.exceptions.AlreadyFoundException;
import com.aswe.communicraft.mapper.Mapper;
import com.aswe.communicraft.models.dto.CraftDto;
import com.aswe.communicraft.models.entities.CraftEntity;
import com.aswe.communicraft.repositories.CraftRepository;
import com.aswe.communicraft.services.CraftService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CraftServiceImpl implements CraftService {

    private final Mapper<CraftEntity, CraftDto> mapper;

    private final CraftRepository craftRepository;
    @Override
    public void addCraft(CraftDto craftDto) throws AlreadyFoundException {
        Optional<CraftEntity> craft= craftRepository.findByName(craftDto.getName());

        if (craft.isPresent())
            throw new AlreadyFoundException("Name already exists. (copyrights issues)");

        CraftEntity craftEntity = mapper.toEntity(craftDto, CraftEntity.class);

        craftRepository.save(craftEntity);
    }
}

