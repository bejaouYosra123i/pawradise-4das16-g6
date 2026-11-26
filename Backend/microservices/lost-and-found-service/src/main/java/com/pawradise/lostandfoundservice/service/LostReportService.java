package com.pawradise.lostandfoundservice.service;

import com.pawradise.lostandfoundservice.entity.LostReport;
import com.pawradise.lostandfoundservice.exception.NotFoundException;
import com.pawradise.lostandfoundservice.repository.LostReportRepository;
import com.pawradise.lostandfoundservice.util.Coordinates;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LostReportService {

    private final LostReportRepository repository;
    private final ReportCleanup cleanup;

    public LostReportService(LostReportRepository repository, ReportCleanup cleanup) {
        this.repository = repository;
        this.cleanup = cleanup;
    }

    @Transactional
    public LostReport create(LostReport report) {
        validate(report);
        report.setId(null);        // never trust an id sent by the client on creation
        report.setStatus(null);    // always starts OPEN (see Report.onCreate)
        report.setOpenedAt(null);
        report.setClosedAt(null);
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
        validate(data);
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
        cleanup.deleteChildrenOf(id);
        repository.delete(report);
    }

    private void validate(LostReport report) {
        if (report.getAnimalType() == null || report.getAnimalType().isBlank()) {
            throw new IllegalArgumentException("animalType is required");
        }
        Coordinates.validate(report.getLatitude(), report.getLongitude());
    }
}
