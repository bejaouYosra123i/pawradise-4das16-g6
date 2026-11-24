package com.pawradise.animal_rescue_service.dto;

import com.pawradise.animal_rescue_service.enums.ResourceStatus;

public record ResourceRequirementResponseDTO(
        Long id,
        String itemName,
        Integer quantity,
        ResourceStatus status,
        Long missionId
) {
}