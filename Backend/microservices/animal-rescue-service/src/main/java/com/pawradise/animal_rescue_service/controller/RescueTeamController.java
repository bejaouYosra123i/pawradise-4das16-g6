package com.pawradise.animal_rescue_service.controller;

import com.pawradise.animal_rescue_service.dto.RescueTeamCreateDTO;
import com.pawradise.animal_rescue_service.dto.RescueTeamResponseDTO;
import com.pawradise.animal_rescue_service.enums.TeamStatus;
import com.pawradise.animal_rescue_service.service.RescueTeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rescue/teams")
@RequiredArgsConstructor
public class RescueTeamController {

    private final RescueTeamService service;

    @PostMapping
    public ResponseEntity<RescueTeamResponseDTO> create(
            @Valid @RequestBody RescueTeamCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @GetMapping
    public List<RescueTeamResponseDTO> list(
            @RequestParam(required = false) TeamStatus status) {
        return service.findAll(status);
    }

    @GetMapping("/{id}")
    public RescueTeamResponseDTO get(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public RescueTeamResponseDTO update(
            @PathVariable Long id, @Valid @RequestBody RescueTeamCreateDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}