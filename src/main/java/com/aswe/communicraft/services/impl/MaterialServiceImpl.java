package com.aswe.communicraft.services.impl;

import com.aswe.communicraft.exceptions.NotFoundException;
import com.aswe.communicraft.mapper.Mapper;
import com.aswe.communicraft.models.dto.MaterialDto;
import com.aswe.communicraft.models.entities.MaterialEntity;
import com.aswe.communicraft.models.entities.UserEntity;
import com.aswe.communicraft.repositories.MaterialRepository;
import com.aswe.communicraft.repositories.UserRepository;
import com.aswe.communicraft.security.JwtUtils;
import com.aswe.communicraft.services.MaterialService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
/*
 * The MaterialService class provides material-related services.
 */
public class MaterialServiceImpl implements MaterialService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MaterialServiceImpl.class);
    private final JwtUtils jwtUtils;
    private final Mapper<MaterialEntity, MaterialDto> mapper;
    private final UserRepository userRepository;
    private final MaterialRepository materialRepository;
    @Override
    public void addMaterial(MaterialDto materialDto, HttpServletRequest request) throws NotFoundException {
        String token = request.getHeader("Authorization");
        int id = jwtUtils.getIdFromJwtToken(token);

        Optional<UserEntity> user = userRepository.findById(id);

        if (user.isEmpty()){
            throw new NotFoundException("User not found with this id :" + id);
        }

        MaterialEntity material = mapper.toEntity(materialDto,MaterialEntity.class);
        material.setUser(user.get());
        materialRepository.save(material);
    }

    @Override
    public List<MaterialDto> listAvailableMaterials() throws NotFoundException {
        List<MaterialEntity> materials = materialRepository.findAll();

        if(materials.isEmpty()) {
            LOGGER.error("No materials are in the system at the moment.");
            throw new NotFoundException("No materials are in the system at the moment.");
        }

        return materials.stream()
                .filter(material -> !material.isUsed())
                .map(material -> mapper.toDto(material, MaterialDto.class)).toList();
    }

    @Override
    public void rentMaterial(int id) throws NotFoundException {
        Optional<MaterialEntity> material = materialRepository.findById(id);
        if (material.isEmpty()){
            throw new NotFoundException("material not found with this id :" + id);
        }

        if (material.get().isUsed()){
            throw new NotFoundException("material with this id :" + id + " is already used");
        }

        material.get().setUsed(true);
        materialRepository.save(material.get());
    }

    @Override
    public void finishFromMaterial(int id) throws NotFoundException {
        Optional<MaterialEntity> material = materialRepository.findById(id);

        if (material.isEmpty()){
            throw new NotFoundException("material not found with this id :" + id);
        }

        if (!material.get().isUsed()){
            throw new NotFoundException("material with this id :" + id + " is already available");
        }

        material.get().setUsed(false);
        materialRepository.save(material.get());
    }
}
