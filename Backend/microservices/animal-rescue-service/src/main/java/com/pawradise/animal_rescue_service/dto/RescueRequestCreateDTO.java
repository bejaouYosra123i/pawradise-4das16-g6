package com.pawradise.animal_rescue_service.dto;

import com.pawradise.animal_rescue_service.enums.UrgencyLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RescueRequestCreateDTO(
        Long animalId,

        @NotBlank(message = "Le type d'animal est obligatoire")
        String animalType,

        @NotBlank(message = "Le nom du déclarant est obligatoire")
        String reporterName,

        String reporterPhone,

        @NotBlank(message = "La description est obligatoire")
        @Size(max = 1000, message = "La description ne doit pas dépasser 1000 caractères")
        String description,

        String photoUrl,

        @NotBlank(message = "L'adresse est obligatoire")
        String locationAddress,

        @NotNull(message = "Le niveau d'urgence est obligatoire")
        UrgencyLevel urgencyLevel
) {
}