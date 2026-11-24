package com.pawradise.animal_rescue_service.repository;

import com.pawradise.animal_rescue_service.entity.RescueMission;
import com.pawradise.animal_rescue_service.enums.MissionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RescueMissionRepository extends JpaRepository<RescueMission, Long> {

    List<RescueMission> findByStatus(MissionStatus status);
}