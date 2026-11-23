package com.pawradise.animal_rescue_service.controller;

import com.pawradise.animal_rescue_service.dto.RescueRequestCreateDTO;
import com.pawradise.animal_rescue_service.dto.RescueRequestResponseDTO;
import com.pawradise.animal_rescue_service.enums.RequestStatus;
import com.pawradise.animal_rescue_service.service.RescueRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rescue/requests")
@RequiredArgsConstructor
public class RescueRequestController {

    private final RescueRequestService service;

    @PostMapping
    public ResponseEntity<RescueRequestResponseDTO> create(
            @Valid @RequestBody RescueRequestCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @GetMapping
    public List<RescueRequestResponseDTO> list(
            @RequestParam(required = false) RequestStatus status) {
        return service.findAll(status);
    }

    @GetMapping("/{id}")
    public RescueRequestResponseDTO get(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public RescueRequestResponseDTO update(
            @PathVariable Long id, @Valid @RequestBody RescueRequestCreateDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}