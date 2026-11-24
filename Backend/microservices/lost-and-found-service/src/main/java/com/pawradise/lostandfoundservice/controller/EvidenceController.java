package com.pawradise.lostandfoundservice.controller;

import com.pawradise.lostandfoundservice.entity.Evidence;
import com.pawradise.lostandfoundservice.service.EvidenceService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EvidenceController {

    private final EvidenceService service;

    public EvidenceController(EvidenceService service) {
        this.service = service;
    }

    @PostMapping("/sightings/{sightingId}/evidences")
    @ResponseStatus(HttpStatus.CREATED)
    public Evidence addToSighting(@PathVariable Long sightingId, @RequestBody Evidence evidence) {
        return service.addToSighting(sightingId, evidence);
    }

    @GetMapping("/sightings/{sightingId}/evidences")
    public List<Evidence> findBySighting(@PathVariable Long sightingId) {
        return service.findBySighting(sightingId);
    }

    @PostMapping("/claims/{claimId}/evidences")
    @ResponseStatus(HttpStatus.CREATED)
    public Evidence addToClaim(@PathVariable Long claimId, @RequestBody Evidence evidence) {
        return service.addToClaim(claimId, evidence);
    }

    @GetMapping("/claims/{claimId}/evidences")
    public List<Evidence> findByClaim(@PathVariable Long claimId) {
        return service.findByClaim(claimId);
    }

    @GetMapping("/evidences/{id}")
    public Evidence findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/evidences/{id}")
    public Evidence update(@PathVariable Long id, @RequestBody Evidence evidence) {
        return service.update(id, evidence);
    }

    @DeleteMapping("/evidences/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
