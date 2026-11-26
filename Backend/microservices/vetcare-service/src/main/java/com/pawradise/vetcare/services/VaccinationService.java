package com.pawradise.vetcare.services;

import com.pawradise.vetcare.dto.VaccinationRequest;
import com.pawradise.vetcare.dto.VaccinationResponse;
import com.pawradise.vetcare.dto.VaccinationStatusResponse;
import com.pawradise.vetcare.entities.MedicalRecord;
import com.pawradise.vetcare.entities.Vaccination;
import com.pawradise.vetcare.exceptions.ResourceNotFoundException;
import com.pawradise.vetcare.repositories.MedicalRecordRepository;
import com.pawradise.vetcare.repositories.VaccinationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class VaccinationService {

    private final VaccinationRepository vaccinationRepo;
    private final MedicalRecordRepository recordRepo;

    @Transactional
    public VaccinationResponse add(VaccinationRequest r) {
        MedicalRecord record = recordRepo.findByAnimalId(r.animalId())
                .orElseThrow(() -> new ResourceNotFoundException("No medical record for animal " + r.animalId()));
        Vaccination v = new Vaccination();
        v.setMedicalRecord(record);
        v.setVaccineName(r.vaccineName());
        v.setVaccineType(r.vaccineType());
        v.setBatchNumber(r.batchNumber());
        v.setAdministeredAt(r.administeredAt());
        v.setNextDueDate(r.nextDueDate());
        return VaccinationResponse.from(vaccinationRepo.save(v));
    }

    @Transactional(readOnly = true)
    public VaccinationStatusResponse status(Long animalId) {
        recordRepo.findByAnimalId(animalId)
                .orElseThrow(() -> new ResourceNotFoundException("No medical record for animal " + animalId));

        List<Vaccination> all = vaccinationRepo.findByMedicalRecordAnimalIdOrderByAdministeredAtDesc(animalId);

        // latest shot of each vaccine decides whether it is overdue
        Map<String, Vaccination> latest = new LinkedHashMap<>();
        for (Vaccination v : all) latest.putIfAbsent(v.getVaccineName(), v);

        LocalDate today = LocalDate.now();
        List<String> overdue = latest.values().stream()
                .filter(v -> v.getNextDueDate() != null && v.getNextDueDate().isBefore(today))
                .map(Vaccination::getVaccineName)
                .toList();

        boolean upToDate = !all.isEmpty() && overdue.isEmpty();
        return new VaccinationStatusResponse(animalId, upToDate, overdue,
                all.stream().map(VaccinationResponse::from).toList());
    }
}
