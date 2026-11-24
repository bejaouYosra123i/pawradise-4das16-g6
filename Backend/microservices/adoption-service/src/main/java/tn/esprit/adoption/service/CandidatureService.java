package tn.esprit.adoption.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.adoption.entity.AnnonceAdoption;
import tn.esprit.adoption.entity.Candidature;
import tn.esprit.adoption.entity.StatutCandidature;
import tn.esprit.adoption.exception.ResourceNotFoundException;
import tn.esprit.adoption.repository.AnnonceAdoptionRepository;
import tn.esprit.adoption.repository.CandidatureRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CandidatureService {

    private final CandidatureRepository repository;
    private final AnnonceAdoptionRepository annonceRepository;

    public Candidature create(Long annonceId, Candidature data) {
        AnnonceAdoption annonce = annonceRepository.findById(annonceId)
                .orElseThrow(() -> new ResourceNotFoundException("Annonce introuvable : " + annonceId));
        data.setId(null);
        data.setAnnonce(annonce);
        data.setStatut(StatutCandidature.EN_ATTENTE);
        return repository.save(data);
    }

    @Transactional(readOnly = true)
    public List<Candidature> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Candidature> findByAnnonce(Long annonceId) {
        if (!annonceRepository.existsById(annonceId)) {
            throw new ResourceNotFoundException("Annonce introuvable : " + annonceId);
        }
        return repository.findByAnnonceId(annonceId);
    }

    @Transactional(readOnly = true)
    public Candidature findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidature introuvable : " + id));
    }

    public Candidature update(Long id, Candidature data) {
        Candidature existing = findById(id);
        if (data.getIdCandidat() != null) existing.setIdCandidat(data.getIdCandidat());
        if (data.getMessage() != null) existing.setMessage(data.getMessage());
        return repository.save(existing);
    }

    public Candidature changerStatut(Long id, StatutCandidature nouveau) {
        Candidature c = findById(id);
        StatutCandidature actuel = c.getStatut() == null
                ? StatutCandidature.EN_ATTENTE
                : c.getStatut();

        boolean ok = false;
        if (actuel == StatutCandidature.EN_ATTENTE) {
            ok = nouveau == StatutCandidature.EN_EXAMEN
                    || nouveau == StatutCandidature.ANNULEE;
        } else if (actuel == StatutCandidature.EN_EXAMEN) {
            ok = nouveau == StatutCandidature.APPROUVEE
                    || nouveau == StatutCandidature.REFUSEE
                    || nouveau == StatutCandidature.ANNULEE;
        }

        if (!ok) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Transition interdite : " + actuel + " -> " + nouveau);
        }

        c.setStatut(nouveau);
        return repository.save(c);
    }

    public void delete(Long id) {
        repository.delete(findById(id));
    }
}