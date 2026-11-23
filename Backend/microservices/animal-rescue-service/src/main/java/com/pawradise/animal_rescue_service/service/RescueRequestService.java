package com.pawradise.animal_rescue_service.service;

import com.pawradise.animal_rescue_service.dto.RescueRequestCreateDTO;
import com.pawradise.animal_rescue_service.dto.RescueRequestResponseDTO;
import com.pawradise.animal_rescue_service.entity.RescueRequest;
import com.pawradise.animal_rescue_service.enums.RequestStatus;
import com.pawradise.animal_rescue_service.exception.ResourceNotFoundException;
import com.pawradise.animal_rescue_service.mapper.RescueRequestMapper;
import com.pawradise.animal_rescue_service.repository.RescueRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RescueRequestService {

    private final RescueRequestRepository repository;
    private final RescueRequestMapper mapper;

    @Transactional
    public RescueRequestResponseDTO create(RescueRequestCreateDTO dto) {
        RescueRequest saved = repository.save(mapper.toEntity(dto));
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<RescueRequestResponseDTO> findAll(RequestStatus status) {
        List<RescueRequest> requests =
                status == null ? repository.findAll() : repository.findByStatus(status);
        return requests.stream().map(mapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public RescueRequestResponseDTO findById(Long id) {
        return mapper.toResponse(getEntity(id));
    }

    @Transactional
    public RescueRequestResponseDTO update(Long id, RescueRequestCreateDTO dto) {
        RescueRequest existing = getEntity(id);
        mapper.updateEntity(existing, dto);
        existing.triage();
        return mapper.toResponse(repository.save(existing));
    }

    @Transactional
    public void delete(Long id) {
        repository.delete(getEntity(id));
    }

    private RescueRequest getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Demande introuvable : " + id));
    }
}