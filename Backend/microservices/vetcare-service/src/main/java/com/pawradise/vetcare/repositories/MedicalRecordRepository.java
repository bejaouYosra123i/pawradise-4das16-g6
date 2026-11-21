package com.pawradise.vetcare.repositories;

import com.pawradise.vetcare.entities.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    Optional<MedicalRecord> findByAnimalId(Long animalId);
    boolean existsByAnimalId(Long animalId);
}
