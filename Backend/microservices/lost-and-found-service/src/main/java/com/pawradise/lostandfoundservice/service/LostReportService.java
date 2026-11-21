package com.pawradise.lostandfoundservice.service;

import com.pawradise.lostandfoundservice.entity.LostReport;
import com.pawradise.lostandfoundservice.exception.NotFoundException;
import com.pawradise.lostandfoundservice.repository.LostReportRepository;
import com.pawradise.lostandfoundservice.repository.OwnershipClaimRepository;
import com.pawradise.lostandfoundservice.repository.SightingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LostReportService {

    private final LostReportRepository repository;
    private final SightingRepository sightingRepository;
    private final OwnershipClaimRepository claimRepository;

    public LostReportService(LostReportRepository repository,
                             SightingRepository sightingRepository,
                             OwnershipClaimRepository claimRepository) {
        this.repository = repository;
        this.sightingRepository = sightingRepository;
        this.claimRepository = claimRepository;
    }

    @Transactional
    public LostReport create(LostReport report) {
        if (report.getAnimalType() == null || report.getAnimalType().isBlank()) {
            throw new IllegalArgumentException("animalType is required");
        }
        report.setId(null); // never trust an id sent by the client on creation
        return repository.save(report);
    }

    @Transactional(readOnly = true)
    public List<LostReport> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public LostReport findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("LostReport " + id + " not found"));
    }

    @Transactional
    public LostReport update(Long id, LostReport data) {
        LostReport existing = findById(id);
        existing.setPetName(data.getPetName());
        existing.setLostDate(data.getLostDate());
        existing.setAnimalType(data.getAnimalType());
        existing.setBreed(data.getBreed());
        existing.setColor(data.getColor());
        existing.setSize(data.getSize());
        existing.setDistinctiveMarks(data.getDistinctiveMarks());
        existing.setDescription(data.getDescription());
        existing.setPhotoUrl(data.getPhotoUrl());
        existing.setCity(data.getCity());
        existing.setLatitude(data.getLatitude());
        existing.setLongitude(data.getLongitude());
        return repository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        LostReport report = findById(id);
        sightingRepository.deleteByReportId(id);
        claimRepository.deleteByReportId(id);
        repository.delete(report);
    }
}