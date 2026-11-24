package com.pawradise.animal_rescue_service.service;

import com.pawradise.animal_rescue_service.dto.ResourceRequirementCreateDTO;
import com.pawradise.animal_rescue_service.dto.ResourceRequirementResponseDTO;
import com.pawradise.animal_rescue_service.entity.RescueMission;
import com.pawradise.animal_rescue_service.entity.ResourceRequirement;
import com.pawradise.animal_rescue_service.enums.ResourceStatus;
import com.pawradise.animal_rescue_service.exception.ResourceNotFoundException;
import com.pawradise.animal_rescue_service.mapper.ResourceRequirementMapper;
import com.pawradise.animal_rescue_service.repository.RescueMissionRepository;
import com.pawradise.animal_rescue_service.repository.ResourceRequirementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResourceRequirementService {

    private final ResourceRequirementRepository repository;
    private final RescueMissionRepository missionRepository;
    private final ResourceRequirementMapper mapper;

    @Transactional
    public ResourceRequirementResponseDTO create(ResourceRequirementCreateDTO dto) {
        RescueMission mission = missionRepository.findById(dto.missionId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Mission introuvable : " + dto.missionId()));

        ResourceRequirement saved = repository.save(mapper.toEntity(dto, mission));
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<ResourceRequirementResponseDTO> findAll(ResourceStatus status, Long missionId) {
        List<ResourceRequirement> resources;
        if (missionId != null) {
            resources = repository.findByMissionId(missionId);
        } else if (status != null) {
            resources = repository.findByStatus(status);
        } else {
            resources = repository.findAll();
        }
        return resources.stream().map(mapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ResourceRequirementResponseDTO findById(Long id) {
        return mapper.toResponse(getEntity(id));
    }

    @Transactional
    public ResourceRequirementResponseDTO update(Long id, ResourceRequirementCreateDTO dto) {
        ResourceRequirement existing = getEntity(id);
        mapper.updateEntity(existing, dto);
        return mapper.toResponse(repository.save(existing));
    }

    @Transactional
    public void delete(Long id) {
        repository.delete(getEntity(id));
    }

    private ResourceRequirement getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Ressource introuvable : " + id));
    }
}