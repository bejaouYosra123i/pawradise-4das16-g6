package com.pawradise.vetcare.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record VaccinationRequest(
        @NotNull Long animalId,
        @NotBlank String vaccineName,
        String vaccineType,
        String batchNumber,
        @NotNull LocalDate administeredAt,
        LocalDate nextDueDate) {}
