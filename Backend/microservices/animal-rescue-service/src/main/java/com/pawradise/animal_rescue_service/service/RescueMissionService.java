package com.pawradise.animal_rescue_service.service;

import com.pawradise.animal_rescue_service.dto.RescueMissionCreateDTO;
import com.pawradise.animal_rescue_service.dto.RescueMissionResponseDTO;
import com.pawradise.animal_rescue_service.entity.RescueMission;
import com.pawradise.animal_rescue_service.entity.RescueRequest;
import com.pawradise.animal_rescue_service.enums.MissionStatus;
import com.pawradise.animal_rescue_service.enums.RequestStatus;
import com.pawradise.animal_rescue_service.exception.ResourceNotFoundException;
import com.pawradise.animal_rescue_service.mapper.RescueMissionMapper;
import com.pawradise.animal_rescue_service.repository.RescueMissionRepository;
import com.pawradise.animal_rescue_service.repository.RescueRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RescueMissionService {

    private final RescueMissionRepository missionRepository;
    private final RescueRequestRepository requestRepository;
    private final RescueMissionMapper mapper;

    @Transactional
    public RescueMissionResponseDTO create(RescueMissionCreateDTO dto) {
        // 1. Récupérer la demande liée
        RescueRequest request = requestRepository.findById(dto.requestId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Demande introuvable : " + dto.requestId()));

        // 2. Créer la mission
        RescueMission mission = mapper.toEntity(dto, request);

        // 3. Marquer la demande comme MISSION_CREATED
        request.setStatus(RequestStatus.MISSION_CREATED);
        requestRepository.save(request);

        // 4. Sauvegarder la mission
        RescueMission saved = missionRepository.save(mission);
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<RescueMissionResponseDTO> findAll(MissionStatus status) {
        List<RescueMission> missions =
                status == null ? missionRepository.findAll()
                        : missionRepository.findByStatus(status);
        return missions.stream().map(mapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public RescueMissionResponseDTO findById(Long id) {
        return mapper.toResponse(getEntity(id));
    }

    @Transactional
    public RescueMissionResponseDTO update(Long id, RescueMissionCreateDTO dto) {
        RescueMission existing = getEntity(id);
        mapper.updateEntity(existing, dto);
        return mapper.toResponse(missionRepository.save(existing));
    }

    @Transactional
    public void delete(Long id) {
        missionRepository.delete(getEntity(id));
    }

    private RescueMission getEntity(Long id) {
        return missionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Mission introuvable : " + id));
    }
}