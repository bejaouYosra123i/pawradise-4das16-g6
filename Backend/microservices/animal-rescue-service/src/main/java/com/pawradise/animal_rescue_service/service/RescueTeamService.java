package com.pawradise.animal_rescue_service.service;

import com.pawradise.animal_rescue_service.dto.RescueTeamCreateDTO;
import com.pawradise.animal_rescue_service.dto.RescueTeamResponseDTO;
import com.pawradise.animal_rescue_service.entity.RescueTeam;
import com.pawradise.animal_rescue_service.enums.TeamStatus;
import com.pawradise.animal_rescue_service.exception.ResourceNotFoundException;
import com.pawradise.animal_rescue_service.mapper.RescueTeamMapper;
import com.pawradise.animal_rescue_service.repository.RescueTeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RescueTeamService {

    private final RescueTeamRepository repository;
    private final RescueTeamMapper mapper;

    @Transactional
    public RescueTeamResponseDTO create(RescueTeamCreateDTO dto) {
        RescueTeam saved = repository.save(mapper.toEntity(dto));
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<RescueTeamResponseDTO> findAll(TeamStatus status) {
        List<RescueTeam> teams =
                status == null ? repository.findAll() : repository.findByStatus(status);
        return teams.stream().map(mapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public RescueTeamResponseDTO findById(Long id) {
        return mapper.toResponse(getEntity(id));
    }

    @Transactional
    public RescueTeamResponseDTO update(Long id, RescueTeamCreateDTO dto) {
        RescueTeam existing = getEntity(id);
        mapper.updateEntity(existing, dto);
        return mapper.toResponse(repository.save(existing));
    }

    @Transactional
    public void delete(Long id) {
        repository.delete(getEntity(id));
    }

    private RescueTeam getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Équipe introuvable : " + id));
    }
}