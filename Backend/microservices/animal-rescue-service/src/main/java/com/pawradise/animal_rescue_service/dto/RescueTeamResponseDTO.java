package com.pawradise.animal_rescue_service.dto;

import com.pawradise.animal_rescue_service.enums.TeamStatus;
import com.pawradise.animal_rescue_service.enums.TeamType;

public record RescueTeamResponseDTO(
        Long id,
        String name,
        TeamType type,
        TeamStatus status,
        String description,
        String baseZone,
        boolean available
) {
}