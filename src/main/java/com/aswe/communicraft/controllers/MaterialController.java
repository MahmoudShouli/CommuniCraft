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

/*
  The MaterialController class is a REST controller that handles material-related requests.
 */

public class MaterialController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MaterialController.class);
    private final MaterialService materialService;

    /**
     * Handles the request to add a craft to the system.
     *
     * @param materialDto  the materialDto object containing material details
     * @param request  the HttpServletRequest object to set the request
     * @return ResponseEntity with success message if the material added successfully
     */

    @PostMapping
    @PreAuthorize("hasAuthority('CRAFTSMAN')")
    public ResponseEntity<String> addMaterial(@RequestBody MaterialDto materialDto , HttpServletRequest request) throws NotFoundException {
        materialService.addMaterial(materialDto,request);
        LOGGER.info("adding material: {}", materialDto.getName());
        return ResponseEntity.ok("new material added Successfully!");
    }

    /**
     * Handles the request to list all available materials in the system.
     *
     * @return ResponseEntity containing list of materials
     */
    @GetMapping
    @PreAuthorize("hasAuthority('CRAFTSMAN')")
    public ResponseEntity<List<MaterialDto>> listAvailableMaterials() throws NotFoundException {
        List<MaterialDto> materials = materialService.listAvailableMaterials();
        LOGGER.info("listing all materials");
        return ResponseEntity.ok(materials);
    }

    /**
     * Handles the request to rent a material from the system.
     *
     * @param materialID  the materialID that has the id needed
     * @return ResponseEntity with success message if the material rent successfully
     */
    @PostMapping("/rent/{materialID}")
    @PreAuthorize("hasAuthority('CRAFTSMAN')")
    public ResponseEntity<String> rentMaterial(@PathVariable int materialID) throws NotFoundException {
        materialService.rentMaterial(materialID);
        LOGGER.info("renting material with id: {}", materialID);
        return ResponseEntity.ok("material rent Successfully!");
    }

    /**
     * Handles the request to finish a material and be available again in the system.
     * @param materialID  the materialID that has the id needed
     * @return ResponseEntity with success message if the material finished successfully
     */

    @PostMapping("/finish/{materialID}")
    @PreAuthorize("hasAuthority('CRAFTSMAN')")
    public ResponseEntity<String> finishFromMaterial(@PathVariable int materialID) throws NotFoundException {
        materialService.finishFromMaterial(materialID);
        LOGGER.info("finishing from material with id: {}", materialID);
        return ResponseEntity.ok("finish from this material Successfully!");
    }
}
