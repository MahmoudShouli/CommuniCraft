package com.aswe.communicraft.controllers;


import com.aswe.communicraft.exceptions.AlreadyFoundException;
import com.aswe.communicraft.models.dto.CraftDto;

import com.aswe.communicraft.services.CraftService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    @PostMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> addCraft(@Valid @RequestBody CraftDto craftDto) throws AlreadyFoundException {

        craftService.addCraft(craftDto);

        return ResponseEntity.ok().body("Craft Created Successfully!");
    }





}
