package com.pawradise.animal_rescue_service.mapper;

import com.pawradise.animal_rescue_service.dto.RescueRequestCreateDTO;
import com.pawradise.animal_rescue_service.dto.RescueRequestResponseDTO;
import com.pawradise.animal_rescue_service.entity.RescueRequest;
import org.springframework.stereotype.Component;

@Component
public class RescueRequestMapper {

    public RescueRequest toEntity(RescueRequestCreateDTO dto) {
        RescueRequest entity = new RescueRequest();
        updateEntity(entity, dto);
        return entity;
    }

    public void updateEntity(RescueRequest entity, RescueRequestCreateDTO dto) {
        entity.setAnimalId(dto.animalId());
        entity.setAnimalType(dto.animalType());
        entity.setReporterName(dto.reporterName());
        entity.setReporterPhone(dto.reporterPhone());
        entity.setDescription(dto.description());
        entity.setPhotoUrl(dto.photoUrl());
        entity.setLocationAddress(dto.locationAddress());
        entity.setUrgencyLevel(dto.urgencyLevel());
    }

    public RescueRequestResponseDTO toResponse(RescueRequest entity) {
        return new RescueRequestResponseDTO(
                entity.getId(),
                entity.getAnimalId(),
                entity.getAnimalType(),
                entity.getReporterName(),
                entity.getReporterPhone(),
                entity.getDescription(),
                entity.getPhotoUrl(),
                entity.getLocationAddress(),
                entity.getUrgencyLevel(),
                entity.getUrgencyScore(),
                entity.getStatus(),
                entity.getReportedAt()
        );
    }
}