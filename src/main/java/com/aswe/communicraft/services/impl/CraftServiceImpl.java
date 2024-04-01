package com.aswe.communicraft.services.impl;


import com.aswe.communicraft.exceptions.AlreadyExistsException;
import com.aswe.communicraft.mapper.Mapper;
import com.aswe.communicraft.models.dto.CraftDto;
import com.aswe.communicraft.models.entities.CraftEntity;
import com.aswe.communicraft.repositories.CraftRepository;
import com.aswe.communicraft.services.CraftService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CraftServiceImpl implements CraftService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CraftServiceImpl.class);

    private final Mapper<CraftEntity, CraftDto> mapper;

    private final CraftRepository craftRepository;
    @Override
    public void addCraft(CraftDto craftDto) throws AlreadyExistsException {
        Optional<CraftEntity> craft= craftRepository.findByName(craftDto.getName());

        if (craft.isPresent()) {
            LOGGER.error("name already exists.");
            throw new AlreadyExistsException("Name already exists.");
        }
        CraftEntity craftEntity = mapper.toEntity(craftDto, CraftEntity.class);

        craftRepository.save(craftEntity);
    }
}

