package com.aswe.communicraft.controllers;


import com.aswe.communicraft.exceptions.AlreadyExistsException;
import com.aswe.communicraft.models.dto.CraftDto;

import com.aswe.communicraft.services.CraftService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/crafts")
@RequiredArgsConstructor
public class CraftController {

    private final CraftService craftService;

    private static final Logger LOGGER = LoggerFactory.getLogger(CraftController.class);
    @PostMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> addCraft(@Valid @RequestBody CraftDto craftDto) throws AlreadyExistsException {

        craftService.addCraft(craftDto);

        LOGGER.info("adding craft: " + craftDto.getName());
        return ResponseEntity.ok().body("Craft Created Successfully!");
    }





}
