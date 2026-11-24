package com.pawradise.animal_rescue_service.dto;

import com.pawradise.animal_rescue_service.enums.TeamType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RescueTeamCreateDTO(

        @NotBlank(message = "Le nom de l'équipe est obligatoire")
        String name,

        @NotNull(message = "Le type d'équipe est obligatoire")
        TeamType type,

        String description,

        @NotBlank(message = "La zone de base est obligatoire")
        String baseZone
) {
}