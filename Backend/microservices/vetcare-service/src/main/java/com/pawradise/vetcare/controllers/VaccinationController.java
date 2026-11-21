package com.pawradise.vetcare.controllers;

import com.pawradise.vetcare.dto.VaccinationRequest;
import com.pawradise.vetcare.dto.VaccinationResponse;
import com.pawradise.vetcare.dto.VaccinationStatusResponse;
import com.pawradise.vetcare.services.VaccinationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vetcare")
@RequiredArgsConstructor
public class VaccinationController {

    private final VaccinationService service;

    @PostMapping("/vaccinations")
    @ResponseStatus(HttpStatus.CREATED)
    public VaccinationResponse add(@Valid @RequestBody VaccinationRequest r) {
        return service.add(r);
    }

    // Called by the Adoption service (REST)
    @GetMapping("/animals/{animalId}/vaccination-status")
    public VaccinationStatusResponse status(@PathVariable Long animalId) {
        return service.status(animalId);
    }
}
