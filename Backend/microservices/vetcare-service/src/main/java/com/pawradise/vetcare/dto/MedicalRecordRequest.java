package com.pawradise.vetcare.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MedicalRecordRequest(
        @NotNull Long animalId,
        @Size(max = 20) String bloodType,
        @Size(max = 1000) String allergies,
        @Size(max = 1000) String chronicConditions) {}
