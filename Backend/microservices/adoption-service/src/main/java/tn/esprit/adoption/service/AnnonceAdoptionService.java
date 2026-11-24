package tn.esprit.adoption.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.adoption.entity.AnnonceAdoption;
import tn.esprit.adoption.exception.ResourceNotFoundException;
import tn.esprit.adoption.repository.AnnonceAdoptionRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AnnonceAdoptionService {

    private final AnnonceAdoptionRepository repository;

    @Transactional(readOnly = true)
    public List<AnnonceAdoption> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public List<AnnonceAdoption> findDisponibles() {
        return repository.findByDisponibleTrue();
    }

    @Transactional(readOnly = true)
    public AnnonceAdoption findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Annonce introuvable : " + id));
    }

    public AnnonceAdoption create(AnnonceAdoption annonce) {
        annonce.setId(null);
        if (annonce.getDateAnnonce() == null) {
            annonce.setDateAnnonce(LocalDate.now());
        }
        return repository.save(annonce);
    }

    public AnnonceAdoption update(Long id, AnnonceAdoption data) {
        AnnonceAdoption existing = findById(id);
        existing.setIdAnimal(data.getIdAnimal());
        existing.setIdRefuge(data.getIdRefuge());
        existing.setDescription(data.getDescription());
        if (data.getDisponible() != null) {
            existing.setDisponible(data.getDisponible());
        }
        return repository.save(existing);
    }

    public void delete(Long id) {
        repository.delete(findById(id));
    }

    public AnnonceAdoption publier(Long id) {
        AnnonceAdoption annonce = findById(id);
        annonce.setDisponible(true);
        return repository.save(annonce);
    }

    public AnnonceAdoption cloturer(Long id) {
        AnnonceAdoption annonce = findById(id);
        annonce.setDisponible(false);
        return repository.save(annonce);
    }
}