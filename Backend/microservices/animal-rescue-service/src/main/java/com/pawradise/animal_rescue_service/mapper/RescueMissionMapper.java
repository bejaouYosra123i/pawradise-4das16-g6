package com.pawradise.animal_rescue_service.mapper;

import com.pawradise.animal_rescue_service.dto.RescueMissionCreateDTO;
import com.pawradise.animal_rescue_service.dto.RescueMissionResponseDTO;
import com.pawradise.animal_rescue_service.entity.RescueMission;
import com.pawradise.animal_rescue_service.entity.RescueRequest;
import org.springframework.stereotype.Component;

@Component
public class RescueMissionMapper {

    public RescueMission toEntity(RescueMissionCreateDTO dto, RescueRequest request) {
        RescueMission mission = new RescueMission();
        mission.setMissionCode(dto.missionCode());
        mission.setAnimalCondition(dto.animalCondition());
        mission.setRequest(request);
        return mission;
    }

    public void updateEntity(RescueMission mission, RescueMissionCreateDTO dto) {
        mission.setMissionCode(dto.missionCode());
        mission.setAnimalCondition(dto.animalCondition());
    }

    public RescueMissionResponseDTO toResponse(RescueMission mission) {
        return new RescueMissionResponseDTO(
                mission.getId(),
                mission.getMissionCode(),
                mission.getStatus(),
                mission.getCreatedAt(),
                mission.getStartedAt(),
                mission.getCompletedAt(),
                mission.getAnimalCondition(),
                mission.getOutcome(),
                mission.getReadyForAdoption(),
                mission.getRequest().getId(),
                mission.getTeam() != null ? mission.getTeam().getId() : null
        );
    }
}