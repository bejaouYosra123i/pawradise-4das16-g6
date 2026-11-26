package com.pawradise.lostandfoundservice.service;

import com.pawradise.lostandfoundservice.entity.Evidence;
import com.pawradise.lostandfoundservice.entity.Report;
import com.pawradise.lostandfoundservice.entity.Sighting;
import com.pawradise.lostandfoundservice.exception.NotFoundException;
import com.pawradise.lostandfoundservice.repository.ReportRepository;
import com.pawradise.lostandfoundservice.repository.SightingRepository;
import com.pawradise.lostandfoundservice.util.Coordinates;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SightingService {

    private final SightingRepository sightings;
    private final ReportRepository reports;

    public SightingService(SightingRepository sightings, ReportRepository reports) {
        this.sightings = sightings;
        this.reports = reports;
    }

    /** POST /api/reports/{id}/sightings -> 201, 400, 404. Evidences can be sent in the same body (optional). */
    @Transactional
    public Sighting create(Long reportId, Sighting data) {
        Report report = reports.findById(reportId)
                .orElseThrow(() -> new NotFoundException("Report " + reportId + " not found"));
        validate(data);

        List<Evidence> sent = data.getEvidences() != null ? new ArrayList<>(data.getEvidences()) : List.of();
        data.setEvidences(new ArrayList<>());
        for (Evidence e : sent) {
            EvidenceService.validate(e);
            e.setId(null);
            data.addEvidence(e);
        }

        data.setId(null);
        data.setReport(report);
        return sightings.save(data);
    }

    @Transactional(readOnly = true)
    public List<Sighting> findByReport(Long reportId) {
        if (!reports.existsById(reportId)) {
            throw new NotFoundException("Report " + reportId + " not found");
        }
        return sightings.findByReport_IdOrderBySeenAtDesc(reportId);
    }

    @Transactional(readOnly = true)
    public Sighting findById(Long id) {
        return sightings.findById(id)
                .orElseThrow(() -> new NotFoundException("Sighting " + id + " not found"));
    }

    /** Updates the sighting fields only. Evidences are managed through /api/sightings/{id}/evidences. */
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
        sightings.delete(findById(id)); // its evidences are deleted by cascade
    }

    private void validate(Sighting data) {
        if (data.getObserverId() == null || data.getObserverId().isBlank()) {
            throw new IllegalArgumentException("observerId is required");
        }
        if (data.getSeenAt() != null && data.getSeenAt().isAfter(LocalDateTime.now().plusMinutes(5))) {
            throw new IllegalArgumentException("seenAt cannot be in the future");
        }
        Coordinates.validate(data.getLatitude(), data.getLongitude());
    }
}
