package com.aswe.communicraft.controllers;

import com.aswe.communicraft.exceptions.NotFoundException;
import com.aswe.communicraft.models.dto.MaterialDto;
import com.aswe.communicraft.services.MaterialService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/materials")
@RequiredArgsConstructor
public class MaterialController {

    private final MaterialService materialService;

    @PostMapping
    @PreAuthorize("hasAuthority('CRAFTSMAN')")
    public ResponseEntity<String> addMaterial(@RequestBody MaterialDto materialDto , HttpServletRequest request) throws NotFoundException {
        materialService.addMaterial(materialDto,request);
        return ResponseEntity.ok("new material added Successfully!");
    }
    @GetMapping
    @PreAuthorize("hasAuthority('CRAFTSMAN')")
    public ResponseEntity<List<MaterialDto>> ListAvailableMaterials() throws NotFoundException {
        List<MaterialDto> materials = materialService.listAvailableMaterials();
        return ResponseEntity.ok(materials);
    }
    @PostMapping("/rent/{materialID}")
    @PreAuthorize("hasAuthority('CRAFTSMAN')")
    public ResponseEntity<String> rentMaterial(@PathVariable int materialID) throws NotFoundException {
        materialService.rentMaterial(materialID);
        return ResponseEntity.ok("material rent Successfully!");
    }
    @PostMapping("/finish/{materialID}")
    @PreAuthorize("hasAuthority('CRAFTSMAN')")
    public ResponseEntity<String> finishFromMaterial(@PathVariable int materialID) throws NotFoundException {
        materialService.finishFromMaterial(materialID);
        return ResponseEntity.ok("finish from this material Successfully!");
    }
}
