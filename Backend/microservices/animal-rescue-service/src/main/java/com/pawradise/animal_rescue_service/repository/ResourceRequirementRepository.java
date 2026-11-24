package com.pawradise.animal_rescue_service.repository;

import com.pawradise.animal_rescue_service.entity.ResourceRequirement;
import com.pawradise.animal_rescue_service.enums.ResourceStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResourceRequirementRepository extends JpaRepository<ResourceRequirement, Long> {

    List<ResourceRequirement> findByStatus(ResourceStatus status);

    List<ResourceRequirement> findByMissionId(Long missionId);
}