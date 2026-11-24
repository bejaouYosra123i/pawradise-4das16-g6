package com.pawradise.animal_rescue_service.controller;

import com.pawradise.animal_rescue_service.dto.ResourceRequirementCreateDTO;
import com.pawradise.animal_rescue_service.dto.ResourceRequirementResponseDTO;
import com.pawradise.animal_rescue_service.enums.ResourceStatus;
import com.pawradise.animal_rescue_service.service.ResourceRequirementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rescue/resources")
@RequiredArgsConstructor
public class ResourceRequirementController {

    private final ResourceRequirementService service;

    @PostMapping
    public ResponseEntity<ResourceRequirementResponseDTO> create(
            @Valid @RequestBody ResourceRequirementCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @GetMapping
    public List<ResourceRequirementResponseDTO> list(
            @RequestParam(required = false) ResourceStatus status,
            @RequestParam(required = false) Long missionId) {
        return service.findAll(status, missionId);
    }

    @GetMapping("/{id}")
    public ResourceRequirementResponseDTO get(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public ResourceRequirementResponseDTO update(
            @PathVariable Long id, @Valid @RequestBody ResourceRequirementCreateDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}