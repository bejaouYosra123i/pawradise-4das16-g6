package tn.esprit.adoption.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.adoption.entity.Candidature;

import java.util.List;

public interface CandidatureRepository extends JpaRepository<Candidature, Long> {
    List<Candidature> findByAnnonceId(Long annonceId);
}