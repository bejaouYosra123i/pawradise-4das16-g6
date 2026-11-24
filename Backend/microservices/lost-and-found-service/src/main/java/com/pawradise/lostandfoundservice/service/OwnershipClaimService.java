package com.pawradise.lostandfoundservice.service;

import com.pawradise.lostandfoundservice.entity.Evidence;
import com.pawradise.lostandfoundservice.entity.FoundReport;
import com.pawradise.lostandfoundservice.entity.OwnershipClaim;
import com.pawradise.lostandfoundservice.entity.Report;
import com.pawradise.lostandfoundservice.enums.CaseStatus;
import com.pawradise.lostandfoundservice.enums.ClaimStatus;
import com.pawradise.lostandfoundservice.exception.NotFoundException;
import com.pawradise.lostandfoundservice.repository.OwnershipClaimRepository;
import com.pawradise.lostandfoundservice.repository.ReportRepository;
import com.pawradise.lostandfoundservice.util.Coordinates;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OwnershipClaimService {

    private final OwnershipClaimRepository claims;
    private final ReportRepository reports;

    public OwnershipClaimService(OwnershipClaimRepository claims, ReportRepository reports) {
        this.claims = claims;
        this.reports = reports;
    }

    /**
     * POST /api/reports/{id}/claims -> 201, 400, 404, 409
     * Someone says "this found animal is mine". The body must contain at least one evidence (UML 1..*).
     */
    @Transactional
    public OwnershipClaim create(Long reportId, OwnershipClaim data) {
        Report report = reports.findById(reportId)
                .orElseThrow(() -> new NotFoundException("Report " + reportId + " not found"));

        if (data.getClaimantId() == null || data.getClaimantId().isBlank()) {
            throw new IllegalArgumentException("claimantId is required");
        }
        Coordinates.validate(data.getLatitude(), data.getLongitude());
        if (data.getEvidences() == null || data.getEvidences().isEmpty()) {
            throw new IllegalArgumentException("At least one evidence is required (evidences: [{type, fileUrl}])");
        }
        data.getEvidences().forEach(EvidenceService::validate);

        if (!(report instanceof FoundReport)) {
            throw new IllegalStateException("Ownership claims can only be made on a FOUND report");
        }
        if (report.isFinished()) {
            throw new IllegalStateException("Report " + reportId + " is already " + report.getStatus());
        }
        if (claims.existsByReport_IdAndClaimantIdAndStatus(reportId, data.getClaimantId(), ClaimStatus.PENDING)) {
            throw new IllegalStateException("Claimant " + data.getClaimantId() + " already has a pending claim on this report");
        }

        List<Evidence> sent = new ArrayList<>(data.getEvidences());
        data.setEvidences(new ArrayList<>());
        for (Evidence e : sent) {
            e.setId(null);
            data.addEvidence(e);
        }
        data.setId(null);
        data.setStatus(ClaimStatus.PENDING);
        data.setCreatedAt(null);
        data.setReport(report);
        return claims.save(data);
    }

    @Transactional(readOnly = true)
    public List<OwnershipClaim> findByReport(Long reportId) {
        if (!reports.existsById(reportId)) {
            throw new NotFoundException("Report " + reportId + " not found");
        }
        return claims.findByReport_Id(reportId);
    }

    @Transactional(readOnly = true)
    public OwnershipClaim findById(Long id) {
        return claims.findById(id)
                .orElseThrow(() -> new NotFoundException("Claim " + id + " not found"));
    }

    /** Only a PENDING claim can be edited (message / position). Evidences have their own endpoints. */
    @Transactional
    public OwnershipClaim update(Long id, OwnershipClaim data) {
        OwnershipClaim existing = findById(id);
        existing.requirePending();
        Coordinates.validate(data.getLatitude(), data.getLongitude());
        existing.setMessage(data.getMessage());
        existing.setLatitude(data.getLatitude());
        existing.setLongitude(data.getLongitude());
        return claims.save(existing);
    }

    /** An ACCEPTED claim is part of the case history and cannot be deleted. */
    @Transactional
    public void delete(Long id) {
        OwnershipClaim claim = findById(id);
        if (claim.getStatus() == ClaimStatus.ACCEPTED) {
            throw new IllegalStateException("An accepted claim cannot be deleted");
        }
        claims.delete(claim);
    }

    /** Accepting a claim marks the report CLAIMED and rejects the other pending claims. */
    @Transactional
    public OwnershipClaim accept(Long id) {
        OwnershipClaim claim = findById(id);
        Report report = claim.getReport();
        if (report.isFinished()) {
            throw new IllegalStateException("Report " + report.getId() + " is already " + report.getStatus());
        }
        claim.accept();
        report.setStatus(CaseStatus.CLAIMED); // managed entity: saved automatically at commit

        for (OwnershipClaim other : claims.findByReport_IdAndStatus(report.getId(), ClaimStatus.PENDING)) {
            if (!other.getId().equals(claim.getId())) {
                other.reject();
            }
        }
        return claims.save(claim);
    }

    @Transactional
    public OwnershipClaim reject(Long id) {
        OwnershipClaim claim = findById(id);
        claim.reject();
        return claims.save(claim);
    }
}
