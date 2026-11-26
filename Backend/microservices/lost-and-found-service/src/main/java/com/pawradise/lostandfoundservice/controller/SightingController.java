package com.pawradise.lostandfoundservice.controller;

import com.pawradise.lostandfoundservice.entity.Sighting;
import com.pawradise.lostandfoundservice.service.SightingService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SightingController {

    private final SightingService service;

    public SightingController(SightingService service) {
        this.service = service;
    }

    @PostMapping("/reports/{reportId}/sightings")
    @ResponseStatus(HttpStatus.CREATED)
    public Sighting create(@PathVariable Long reportId, @RequestBody Sighting sighting) {
        return service.create(reportId, sighting);
    }

    @GetMapping("/reports/{reportId}/sightings")
    public List<Sighting> findByReport(@PathVariable Long reportId) {
        return service.findByReport(reportId);
    }

    @GetMapping("/sightings/{id}")
    public Sighting findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/sightings/{id}")
    public Sighting update(@PathVariable Long id, @RequestBody Sighting sighting) {
        return service.update(id, sighting);
    }

    @DeleteMapping("/sightings/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}