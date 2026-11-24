package com.pawradise.animal_rescue_service.mapper;

import com.pawradise.animal_rescue_service.dto.RescueTeamCreateDTO;
import com.pawradise.animal_rescue_service.dto.RescueTeamResponseDTO;
import com.pawradise.animal_rescue_service.entity.RescueTeam;
import org.springframework.stereotype.Component;

@Component
public class RescueTeamMapper {

    public RescueTeam toEntity(RescueTeamCreateDTO dto) {
        RescueTeam team = new RescueTeam();
        updateEntity(team, dto);
        return team;
    }

    public void updateEntity(RescueTeam team, RescueTeamCreateDTO dto) {
        team.setName(dto.name());
        team.setType(dto.type());
        team.setDescription(dto.description());
        team.setBaseZone(dto.baseZone());
    }

    public RescueTeamResponseDTO toResponse(RescueTeam team) {
        return new RescueTeamResponseDTO(
                team.getId(),
                team.getName(),
                team.getType(),
                team.getStatus(),
                team.getDescription(),
                team.getBaseZone(),
                team.isAvailable()
        );
    }
}