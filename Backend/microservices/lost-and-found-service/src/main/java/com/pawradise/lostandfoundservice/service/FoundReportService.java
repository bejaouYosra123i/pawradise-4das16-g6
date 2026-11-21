package com.pawradise.lostandfoundservice.service;

import com.pawradise.lostandfoundservice.entity.FoundReport;
import com.pawradise.lostandfoundservice.exception.NotFoundException;
import com.pawradise.lostandfoundservice.repository.FoundReportRepository;
import com.pawradise.lostandfoundservice.repository.OwnershipClaimRepository;
import com.pawradise.lostandfoundservice.repository.SightingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FoundReportService {

    private final FoundReportRepository repository;
    private final SightingRepository sightingRepository;
    private final OwnershipClaimRepository claimRepository;

    public FoundReportService(FoundReportRepository repository,
                              SightingRepository sightingRepository,
                              OwnershipClaimRepository claimRepository) {
        this.repository = repository;
        this.sightingRepository = sightingRepository;
        this.claimRepository = claimRepository;
    }

    @Transactional
    public FoundReport create(FoundReport report) {
        if (report.getAnimalType() == null || report.getAnimalType().isBlank()) {
            throw new IllegalArgumentException("animalType is required");
        }
        report.setId(null);                      // never trust an id sent by the client
        report.setRescueReferralSent(false);     // managed by the system, not the client
        if (report.getInjured() == null) {
            report.setInjured(false);
        }
        return repository.save(report);
    }

    @Transactional(readOnly = true)
    public List<FoundReport> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public FoundReport findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("FoundReport " + id + " not found"));
    }

    @Transactional
    public FoundReport update(Long id, FoundReport data) {
        FoundReport existing = findById(id);
        existing.setFoundDate(data.getFoundDate());
        existing.setInjured(data.getInjured() != null ? data.getInjured() : false);
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
        FoundReport report = findById(id);
        sightingRepository.deleteByReportId(id);
        claimRepository.deleteByReportId(id);
        repository.delete(report);
    }
}