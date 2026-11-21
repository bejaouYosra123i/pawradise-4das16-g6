package com.pawradise.vetcare.controllers;

import com.pawradise.vetcare.dto.MedicalRecordRequest;
import com.pawradise.vetcare.dto.MedicalRecordUpdateRequest;
import com.pawradise.vetcare.entities.MedicalRecord;
import com.pawradise.vetcare.services.MedicalRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/vetcare")
@RequiredArgsConstructor
public class MedicalRecordController {

    private final MedicalRecordService service;

    @PostMapping("/medical-records")
    @ResponseStatus(HttpStatus.CREATED)
    public MedicalRecord create(@Valid @RequestBody MedicalRecordRequest r) {
        return service.create(r);
    }

    @GetMapping("/medical-records")
    public List<MedicalRecord> list() { return service.findAll(); }

    @GetMapping("/medical-records/{id}")
    public MedicalRecord get(@PathVariable Long id) { return service.findById(id); }

    @PutMapping("/medical-records/{id}")
    public MedicalRecord update(@PathVariable Long id, @Valid @RequestBody MedicalRecordUpdateRequest r) {
        return service.update(id, r);
    }

    @DeleteMapping("/medical-records/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { service.delete(id); }

    // Called by the Adoption service (REST)
    @GetMapping("/animals/{animalId}/medical-record")
    public MedicalRecord byAnimal(@PathVariable Long animalId) {
        return service.findByAnimalId(animalId);
    }
}
