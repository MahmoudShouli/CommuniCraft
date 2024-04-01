package com.aswe.communicraft.controllers;

import com.aswe.communicraft.exceptions.NotFoundException;
import com.aswe.communicraft.models.dto.MaterialDto;
import com.aswe.communicraft.services.MaterialService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/materials")
@RequiredArgsConstructor
public class MaterialController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MaterialController.class);

    private final MaterialService materialService;

    @PostMapping
    @PreAuthorize("hasAuthority('CRAFTSMAN')")
    public ResponseEntity<String> addMaterial(@RequestBody MaterialDto materialDto , HttpServletRequest request) throws NotFoundException {
        materialService.addMaterial(materialDto,request);
        LOGGER.info("adding material: {}", materialDto.getName());
        return ResponseEntity.ok("new material added Successfully!");
    }
    @GetMapping
    @PreAuthorize("hasAuthority('CRAFTSMAN')")
    public ResponseEntity<List<MaterialDto>> listAvailableMaterials() throws NotFoundException {
        List<MaterialDto> materials = materialService.listAvailableMaterials();
        LOGGER.info("listing all materials");
        return ResponseEntity.ok(materials);
    }
    @PostMapping("/rent/{materialID}")
    @PreAuthorize("hasAuthority('CRAFTSMAN')")
    public ResponseEntity<String> rentMaterial(@PathVariable int materialID) throws NotFoundException {
        materialService.rentMaterial(materialID);
        LOGGER.info("renting material with id: {}", materialID);
        return ResponseEntity.ok("material rent Successfully!");
    }
    @PostMapping("/finish/{materialID}")
    @PreAuthorize("hasAuthority('CRAFTSMAN')")
    public ResponseEntity<String> finishFromMaterial(@PathVariable int materialID) throws NotFoundException {
        materialService.finishFromMaterial(materialID);
        LOGGER.info("finishing from material with id: {}", materialID);
        return ResponseEntity.ok("finish from this material Successfully!");
    }
}
