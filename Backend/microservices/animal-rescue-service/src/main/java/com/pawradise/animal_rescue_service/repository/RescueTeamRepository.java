package com.pawradise.animal_rescue_service.repository;

import com.pawradise.animal_rescue_service.entity.RescueTeam;
import com.pawradise.animal_rescue_service.enums.TeamStatus;
import com.pawradise.animal_rescue_service.enums.TeamType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RescueTeamRepository extends JpaRepository<RescueTeam, Long> {

    List<RescueTeam> findByStatus(TeamStatus status);

    List<RescueTeam> findByType(TeamType type);
}