package com.pawradise.animal_rescue_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RescueMissionCreateDTO(

        @NotNull(message = "L'identifiant de la demande est obligatoire")
        Long requestId,

        @NotBlank(message = "Le code mission est obligatoire")
        String missionCode,

        String animalCondition
) {
}