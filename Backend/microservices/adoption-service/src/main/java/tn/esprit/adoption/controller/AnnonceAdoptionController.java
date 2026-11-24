package tn.esprit.adoption.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.adoption.entity.AnnonceAdoption;
import tn.esprit.adoption.service.AnnonceAdoptionService;

import java.util.List;

@RestController
@RequestMapping("/api/annonces")
@RequiredArgsConstructor
public class AnnonceAdoptionController {

    private final AnnonceAdoptionService service;

    @GetMapping
    public List<AnnonceAdoption> getAll(@RequestParam(required = false) Boolean disponible) {
        return Boolean.TRUE.equals(disponible) ? service.findDisponibles() : service.findAll();
    }

    @GetMapping("/{id}")
    public AnnonceAdoption getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<AnnonceAdoption> create(@Valid @RequestBody AnnonceAdoption annonce) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(annonce));
    }

    @PutMapping("/{id}")
    public AnnonceAdoption update(@PathVariable Long id, @Valid @RequestBody AnnonceAdoption annonce) {
        return service.update(id, annonce);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @PatchMapping("/{id}/publier")
    public AnnonceAdoption publier(@PathVariable Long id) {
        return service.publier(id);
    }

    @PatchMapping("/{id}/cloturer")
    public AnnonceAdoption cloturer(@PathVariable Long id) {
        return service.cloturer(id);
    }
}