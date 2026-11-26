package com.pawradise.lostandfoundservice.controller;

import com.pawradise.lostandfoundservice.entity.AnimalMatch;
import com.pawradise.lostandfoundservice.service.AnimalMatchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
public class AnimalMatchController {

    private final AnimalMatchService service;

    public AnimalMatchController(AnimalMatchService service) {
        this.service = service;
    }

    // GET /api/matches/{reportId} -> 200 (matches), 204 (none), 404 (unknown report)
    @GetMapping("/{reportId}")
    public ResponseEntity<List<AnimalMatch>> findForReport(@PathVariable Long reportId) {
        List<AnimalMatch> result = service.findForReport(reportId);
        return result.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(result);
    }

    // PUT /api/matches/{id}/confirm -> 200, 404, 409
    @PutMapping("/{id}/confirm")
    public AnimalMatch confirm(@PathVariable Long id) {
        return service.confirm(id);
    }

    // PUT /api/matches/{id}/reject -> 200, 404, 409
    @PutMapping("/{id}/reject")
    public AnimalMatch reject(@PathVariable Long id) {
        return service.reject(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
