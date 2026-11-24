package com.pawradise.animal_rescue_service.mapper;

import com.pawradise.animal_rescue_service.dto.ResourceRequirementCreateDTO;
import com.pawradise.animal_rescue_service.dto.ResourceRequirementResponseDTO;
import com.pawradise.animal_rescue_service.entity.RescueMission;
import com.pawradise.animal_rescue_service.entity.ResourceRequirement;
import org.springframework.stereotype.Component;

@Component
public class ResourceRequirementMapper {

    public ResourceRequirement toEntity(ResourceRequirementCreateDTO dto, RescueMission mission) {
        ResourceRequirement resource = new ResourceRequirement();
        resource.setItemName(dto.itemName());
        resource.setQuantity(dto.quantity());
        resource.setMission(mission);
        return resource;
    }

    public void updateEntity(ResourceRequirement resource, ResourceRequirementCreateDTO dto) {
        resource.setItemName(dto.itemName());
        resource.setQuantity(dto.quantity());
    }

    public ResourceRequirementResponseDTO toResponse(ResourceRequirement resource) {
        return new ResourceRequirementResponseDTO(
                resource.getId(),
                resource.getItemName(),
                resource.getQuantity(),
                resource.getStatus(),
                resource.getMission().getId()
        );
    }
}