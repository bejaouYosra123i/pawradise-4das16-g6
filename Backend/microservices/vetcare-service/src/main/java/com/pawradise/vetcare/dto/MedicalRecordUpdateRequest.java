package com.pawradise.vetcare.dto;

import jakarta.validation.constraints.Size;

public record MedicalRecordUpdateRequest(
        @Size(max = 20) String bloodType,
        @Size(max = 1000) String allergies,
        @Size(max = 1000) String chronicConditions) {}
