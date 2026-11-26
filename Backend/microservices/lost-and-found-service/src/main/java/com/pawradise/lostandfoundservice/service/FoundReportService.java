package com.pawradise.lostandfoundservice.service;

import com.pawradise.lostandfoundservice.entity.FoundReport;
import com.pawradise.lostandfoundservice.exception.NotFoundException;
import com.pawradise.lostandfoundservice.repository.FoundReportRepository;
import com.pawradise.lostandfoundservice.util.Coordinates;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FoundReportService {

    private final FoundReportRepository repository;
    private final ReportCleanup cleanup;

    public FoundReportService(FoundReportRepository repository, ReportCleanup cleanup) {
        this.repository = repository;
        this.cleanup = cleanup;
    }

    @Transactional
    public FoundReport create(FoundReport report) {
        validate(report);
        report.setId(null);                      // never trust an id sent by the client
        report.setStatus(null);                  // always starts OPEN (see Report.onCreate)
        report.setOpenedAt(null);
        report.setClosedAt(null);
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
        validate(data);
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
        cleanup.deleteChildrenOf(id);
        repository.delete(report);
    }

    /**
     * UML: FoundReport.requestRescue().
     * For now it only marks the report; later this is where the RescueReferralRequested
     * message will be published to RabbitMQ for Animal Rescue.
     */
    @Transactional
    public FoundReport requestRescue(Long id) {
        FoundReport report = findById(id);
        report.requestRescue();
        return repository.save(report);
    }

    private void validate(FoundReport report) {
        if (report.getAnimalType() == null || report.getAnimalType().isBlank()) {
            throw new IllegalArgumentException("animalType is required");
        }
        Coordinates.validate(report.getLatitude(), report.getLongitude());
    }
}
