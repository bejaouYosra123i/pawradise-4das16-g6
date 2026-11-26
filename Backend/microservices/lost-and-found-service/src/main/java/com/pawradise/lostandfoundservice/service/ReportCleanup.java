package com.pawradise.lostandfoundservice.service;

import com.pawradise.lostandfoundservice.repository.AnimalMatchRepository;
import com.pawradise.lostandfoundservice.repository.OwnershipClaimRepository;
import com.pawradise.lostandfoundservice.repository.SightingRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Deletes everything attached to a report before the report itself is deleted
 * (otherwise PostgreSQL refuses because of the foreign keys).
 * Evidences are removed automatically through the cascade on Sighting / OwnershipClaim.
 */
@Component
public class ReportCleanup {

    private final SightingRepository sightings;
    private final OwnershipClaimRepository claims;
    private final AnimalMatchRepository matches;

    public ReportCleanup(SightingRepository sightings, OwnershipClaimRepository claims, AnimalMatchRepository matches) {
        this.sightings = sightings;
        this.claims = claims;
        this.matches = matches;
    }

    @Transactional
    public void deleteChildrenOf(Long reportId) {
        sightings.deleteAll(sightings.findByReport_Id(reportId));
        claims.deleteAll(claims.findByReport_Id(reportId));
        matches.deleteAll(matches.findByLostReport_IdOrFoundReport_Id(reportId, reportId));
    }
}
