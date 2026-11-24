package com.pawradise.lostandfoundservice.controller;

import com.pawradise.lostandfoundservice.entity.OwnershipClaim;
import com.pawradise.lostandfoundservice.entity.Report;
import com.pawradise.lostandfoundservice.enums.CaseStatus;
import com.pawradise.lostandfoundservice.exception.NotFoundException;
import com.pawradise.lostandfoundservice.repository.OwnershipClaimRepository;
import com.pawradise.lostandfoundservice.repository.ReportRepository;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Endpoints shared by lost and found reports: ownership claims and status.
 * Sightings now live in SightingController.
 */
@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportRepository reports;
    private final OwnershipClaimRepository claims;

    public ReportController(ReportRepository reports, OwnershipClaimRepository claims) {
        this.reports = reports;
        this.claims = claims;
    }

    private Report getReport(Long id) {
        return reports.findById(id)
                .orElseThrow(() -> new NotFoundException("Report " + id + " not found"));
    }

    // POST /api/reports/{id}/claims -> 201, 404, 409
    @PostMapping("/{id}/claims")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public OwnershipClaim addClaim(@PathVariable Long id, @RequestBody OwnershipClaim claim) {
        Report report = getReport(id);
        if (report.getStatus() == CaseStatus.CLOSED || report.getStatus() == CaseStatus.RECOVERED) {
            throw new IllegalStateException("Report " + id + " is already " + report.getStatus());
        }
        claim.setId(null);
        claim.setReport(report);
        return claims.save(claim);
    }

    @GetMapping("/{id}/claims")
    @Transactional(readOnly = true)
    public List<OwnershipClaim> listClaims(@PathVariable Long id) {
        getReport(id);
        return claims.findByReportId(id);
    }

    // PUT /api/reports/{id}/status  body: {"status":"CLOSED"} -> 200, 400, 404, 409
    @PutMapping("/{id}/status")
    @Transactional
    public Map<String, Object> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Report report = getReport(id);
        CaseStatus newStatus;
        try {
            newStatus = CaseStatus.valueOf(String.valueOf(body.get("status")).toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status. Allowed: OPEN, MATCHED, CLAIMED, RECOVERED, CLOSED");
        }
        if (report.getStatus() == CaseStatus.CLOSED) {
            throw new IllegalStateException("Report " + id + " is already CLOSED");
        }
        if (newStatus == CaseStatus.CLOSED) {
            report.close();
        } else {
            report.setStatus(newStatus);
        }
        reports.save(report);
        return Map.of("id", report.getId(), "status", report.getStatus());
    }
}