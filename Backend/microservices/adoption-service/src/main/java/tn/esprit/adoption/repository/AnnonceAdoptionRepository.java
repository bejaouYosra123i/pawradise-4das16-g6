package tn.esprit.adoption.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.adoption.entity.AnnonceAdoption;

import java.util.List;

public interface AnnonceAdoptionRepository extends JpaRepository<AnnonceAdoption, Long> {

    List<AnnonceAdoption> findByDisponibleTrue();
}