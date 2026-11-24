package com.pawradise.animal_rescue_service.controller;

import com.pawradise.animal_rescue_service.dto.RescueMissionCreateDTO;
import com.pawradise.animal_rescue_service.dto.RescueMissionResponseDTO;
import com.pawradise.animal_rescue_service.enums.MissionStatus;
import com.pawradise.animal_rescue_service.service.RescueMissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rescue/missions")
@RequiredArgsConstructor
public class RescueMissionController {

    private final RescueMissionService service;

    @PostMapping
    public ResponseEntity<RescueMissionResponseDTO> create(
            @Valid @RequestBody RescueMissionCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @GetMapping
    public List<RescueMissionResponseDTO> list(
            @RequestParam(required = false) MissionStatus status) {
        return service.findAll(status);
    }

    @GetMapping("/{id}")
    public RescueMissionResponseDTO get(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public RescueMissionResponseDTO update(
            @PathVariable Long id, @Valid @RequestBody RescueMissionCreateDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}