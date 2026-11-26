package com.pawradise.vetcare.services;

import com.pawradise.vetcare.dto.MedicalRecordRequest;
import com.pawradise.vetcare.dto.MedicalRecordUpdateRequest;
import com.pawradise.vetcare.entities.MedicalRecord;
import com.pawradise.vetcare.exceptions.ConflictException;
import com.pawradise.vetcare.exceptions.ResourceNotFoundException;
import com.pawradise.vetcare.repositories.MedicalRecordRepository;
import com.pawradise.vetcare.repositories.VaccinationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicalRecordService {

    private final MedicalRecordRepository repo;
    private final VaccinationRepository vaccinationRepo;

    @Transactional
    public MedicalRecord create(MedicalRecordRequest r) {
        if (repo.existsByAnimalId(r.animalId())) {
            throw new ConflictException("A medical record already exists for animal " + r.animalId());
        }
        MedicalRecord m = new MedicalRecord();
        m.setAnimalId(r.animalId());
        m.setBloodType(r.bloodType());
        m.setAllergies(r.allergies());
        m.setChronicConditions(r.chronicConditions());
        return repo.save(m);
    }

    public List<MedicalRecord> findAll() { return repo.findAll(); }

    public MedicalRecord findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medical record " + id + " not found"));
    }

    public MedicalRecord findByAnimalId(Long animalId) {
        return repo.findByAnimalId(animalId)
                .orElseThrow(() -> new ResourceNotFoundException("No medical record for animal " + animalId));
    }

    @Transactional
    public MedicalRecord update(Long id, MedicalRecordUpdateRequest r) {
        MedicalRecord m = findById(id);
        m.setBloodType(r.bloodType());
        m.setAllergies(r.allergies());
        m.setChronicConditions(r.chronicConditions());
        return repo.save(m);
    }

    @Transactional
    public void delete(Long id) {
        MedicalRecord m = findById(id);
        vaccinationRepo.deleteAll(
                vaccinationRepo.findByMedicalRecordAnimalIdOrderByAdministeredAtDesc(m.getAnimalId()));
        repo.delete(m);
    }
}
