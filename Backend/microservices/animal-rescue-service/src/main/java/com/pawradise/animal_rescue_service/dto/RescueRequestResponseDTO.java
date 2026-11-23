package com.pawradise.animal_rescue_service.dto;

import com.pawradise.animal_rescue_service.enums.RequestStatus;
import com.pawradise.animal_rescue_service.enums.UrgencyLevel;

import java.time.LocalDateTime;

public record RescueRequestResponseDTO(
        Long id,
        Long animalId,
        String animalType,
        String reporterName,
        String reporterPhone,
        String description,
        String photoUrl,
        String locationAddress,
        UrgencyLevel urgencyLevel,
        Integer urgencyScore,
        RequestStatus status,
        LocalDateTime reportedAt
) {
}