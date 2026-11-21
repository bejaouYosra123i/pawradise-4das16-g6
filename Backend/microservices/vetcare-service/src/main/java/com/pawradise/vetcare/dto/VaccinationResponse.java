package com.pawradise.vetcare.dto;

import com.pawradise.vetcare.entities.Vaccination;
import java.time.LocalDate;

public record VaccinationResponse(
        Long id, Long animalId, String vaccineName, String vaccineType,
        String batchNumber, LocalDate administeredAt, LocalDate nextDueDate) {

    public static VaccinationResponse from(Vaccination v) {
        return new VaccinationResponse(v.getId(), v.getMedicalRecord().getAnimalId(),
                v.getVaccineName(), v.getVaccineType(), v.getBatchNumber(),
                v.getAdministeredAt(), v.getNextDueDate());
    }
}
