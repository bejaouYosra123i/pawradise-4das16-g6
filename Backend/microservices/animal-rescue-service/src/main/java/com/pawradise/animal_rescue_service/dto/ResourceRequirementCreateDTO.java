package com.pawradise.animal_rescue_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ResourceRequirementCreateDTO(

        @NotNull(message = "L'identifiant de la mission est obligatoire")
        Long missionId,

        @NotBlank(message = "Le nom de la ressource est obligatoire")
        String itemName,

        @NotNull(message = "La quantité est obligatoire")
        @Min(value = 1, message = "La quantité doit être au moins 1")
        Integer quantity
) {
}