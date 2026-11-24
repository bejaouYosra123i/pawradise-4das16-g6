package com.pawradise.lostandfoundservice.service;

import com.pawradise.lostandfoundservice.entity.Report;
import com.pawradise.lostandfoundservice.entity.Sighting;
import com.pawradise.lostandfoundservice.exception.NotFoundException;
import com.pawradise.lostandfoundservice.repository.ReportRepository;
import com.pawradise.lostandfoundservice.repository.SightingRepository;
import com.pawradise.lostandfoundservice.util.Coordinates;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SightingService {

    private final SightingRepository sightings;
    private final ReportRepository reports;

    public SightingService(SightingRepository sightings, ReportRepository reports) {
        this.sightings = sightings;
        this.reports = reports;
    }

    @Transactional
    public Sighting create(Long reportId, Sighting data) {
        Report report = reports.findById(reportId)
                .orElseThrow(() -> new NotFoundException("Report " + reportId + " not found"));
        validate(data);
        data.setId(null);
        data.setReport(report);
        return sightings.save(data);
    }

    @Transactional(readOnly = true)
    public List<Sighting> findByReport(Long reportId) {
        if (!reports.existsById(reportId)) {
            throw new NotFoundException("Report " + reportId + " not found");
        }
        return sightings.findByReportId(reportId);
    }

    @Transactional(readOnly = true)
    public Sighting findById(Long id) {
        return sightings.findById(id)
                .orElseThrow(() -> new NotFoundException("Sighting " + id + " not found"));
    }

    @Transactional
    public Sighting update(Long id, Sighting data) {
        Sighting existing = findById(id);
        validate(data);
        existing.setObserverId(data.getObserverId());
        if (data.getSeenAt() != null) {
            existing.setSeenAt(data.getSeenAt());
        }
        existing.setLatitude(data.getLatitude());
        existing.setLongitude(data.getLongitude());
        existing.setComment(data.getComment());
        return sightings.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        sightings.delete(findById(id));
    }

    private void validate(Sighting data) {
        if (data.getObserverId() == null || data.getObserverId().isBlank()) {
            throw new IllegalArgumentException("observerId is required");
        }
        Coordinates.validate(data.getLatitude(), data.getLongitude());
    }
}