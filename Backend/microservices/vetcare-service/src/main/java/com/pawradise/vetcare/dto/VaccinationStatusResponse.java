package com.pawradise.vetcare.dto;

import java.util.List;

public record VaccinationStatusResponse(
        Long animalId, boolean upToDate, List<String> overdueVaccines,
        List<VaccinationResponse> vaccinations) {}
