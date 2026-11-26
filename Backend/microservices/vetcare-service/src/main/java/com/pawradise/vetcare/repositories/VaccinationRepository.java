package com.pawradise.vetcare.repositories;

import com.pawradise.vetcare.entities.Vaccination;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VaccinationRepository extends JpaRepository<Vaccination, Long> {
    List<Vaccination> findByMedicalRecordAnimalIdOrderByAdministeredAtDesc(Long animalId);
}
