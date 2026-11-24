package com.pawradise.animal_rescue_service.dto;

import com.pawradise.animal_rescue_service.enums.MissionOutcome;
import com.pawradise.animal_rescue_service.enums.MissionStatus;

import java.time.LocalDateTime;

public record RescueMissionResponseDTO(
        Long id,
        String missionCode,
        MissionStatus status,
        LocalDateTime createdAt,
        LocalDateTime startedAt,
        LocalDateTime completedAt,
        String animalCondition,
        MissionOutcome outcome,
        Boolean readyForAdoption,
        Long requestId,
        Long teamId
) {
}