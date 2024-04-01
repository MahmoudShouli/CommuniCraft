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

/**
 * The CraftService class provides craft-related services.
 */
@Service
@RequiredArgsConstructor
public class CraftServiceImpl implements CraftService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CraftServiceImpl.class);
    private final Mapper<CraftEntity, CraftDto> mapper;
    private final CraftRepository craftRepository;

    /**
     * Adds a new craft to the system.
     * The method takes a CraftDto containing all needed details of the craft.
     * It checks if the craft already exists in the system.
     * If its already exists, it throws an AlreadyExistsException
     * otherwise it save the new craft to the system
     *
     * @param craftDto object containing details of the craft
     * @throws AlreadyExistsException if a craft with the same name already exists in the system
     */
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

