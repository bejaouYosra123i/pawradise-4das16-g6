package tn.esprit.adoption.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.esprit.adoption.entity.Candidature;
import tn.esprit.adoption.entity.StatutCandidature;
import tn.esprit.adoption.service.CandidatureService;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CandidatureController {

    private final CandidatureService service;

    @PostMapping("/annonces/{annonceId}/candidatures")
    @ResponseStatus(HttpStatus.CREATED)
    public Candidature create(@PathVariable Long annonceId, @Valid @RequestBody Candidature data) {
        return service.create(annonceId, data);
    }

    @GetMapping("/candidatures")
    public List<Candidature> getAll() {
        return service.findAll();
    }

    @GetMapping("/annonces/{annonceId}/candidatures")
    public List<Candidature> getByAnnonce(@PathVariable Long annonceId) {
        return service.findByAnnonce(annonceId);
    }

    @GetMapping("/candidatures/{id}")
    public Candidature getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/candidatures/{id}")
    public Candidature update(@PathVariable Long id, @RequestBody Candidature data) {
        return service.update(id, data);
    }

    @PatchMapping("/candidatures/{id}/statut")
    public Candidature changerStatut(@PathVariable Long id, @RequestParam StatutCandidature statut) {
        return service.changerStatut(id, statut);
    }

    @DeleteMapping("/candidatures/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}