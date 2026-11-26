package com.pawradise.lostandfoundservice.service;

import com.pawradise.lostandfoundservice.entity.Evidence;
import com.pawradise.lostandfoundservice.entity.OwnershipClaim;
import com.pawradise.lostandfoundservice.entity.Sighting;
import com.pawradise.lostandfoundservice.exception.NotFoundException;
import com.pawradise.lostandfoundservice.repository.EvidenceRepository;
import com.pawradise.lostandfoundservice.repository.OwnershipClaimRepository;
import com.pawradise.lostandfoundservice.repository.SightingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EvidenceService {

    private final EvidenceRepository evidences;
    private final SightingRepository sightings;
    private final OwnershipClaimRepository claims;

    public EvidenceService(EvidenceRepository evidences, SightingRepository sightings, OwnershipClaimRepository claims) {
        this.evidences = evidences;
        this.sightings = sightings;
        this.claims = claims;
    }

    // ---------- attached to a sighting ----------

    @Transactional
    public Evidence addToSighting(Long sightingId, Evidence data) {
        Sighting sighting = sightings.findById(sightingId)
                .orElseThrow(() -> new NotFoundException("Sighting " + sightingId + " not found"));
        validate(data);
        data.setId(null);
        sighting.addEvidence(data);
        return evidences.save(data);
    }

    @Transactional(readOnly = true)
    public List<Evidence> findBySighting(Long sightingId) {
        if (!sightings.existsById(sightingId)) {
            throw new NotFoundException("Sighting " + sightingId + " not found");
        }
        return evidences.findBySighting_Id(sightingId);
    }

    // ---------- attached to an ownership claim ----------

    @Transactional
    public Evidence addToClaim(Long claimId, Evidence data) {
        OwnershipClaim claim = claims.findById(claimId)
                .orElseThrow(() -> new NotFoundException("Claim " + claimId + " not found"));
        claim.requirePending(); // no new proof once the claim is decided
        validate(data);
        data.setId(null);
        claim.addEvidence(data);
        return evidences.save(data);
    }

    @Transactional(readOnly = true)
    public List<Evidence> findByClaim(Long claimId) {
        if (!claims.existsById(claimId)) {
            throw new NotFoundException("Claim " + claimId + " not found");
        }
        return evidences.findByClaim_Id(claimId);
    }

    // ---------- single evidence ----------

    @Transactional(readOnly = true)
    public Evidence findById(Long id) {
        return evidences.findById(id)
                .orElseThrow(() -> new NotFoundException("Evidence " + id + " not found"));
    }

    @Transactional
    public Evidence update(Long id, Evidence data) {
        Evidence existing = findById(id);
        validate(data);
        existing.setType(data.getType());
        existing.setFileUrl(data.getFileUrl());
        existing.setScore(data.getScore());
        return evidences.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        Evidence evidence = findById(id);
        OwnershipClaim claim = evidence.getClaim();
        if (claim != null) {
            if (claim.getEvidences().size() <= 1) {
                throw new IllegalStateException("A claim must keep at least one evidence");
            }
            claim.getEvidences().remove(evidence); // orphanRemoval deletes it
            return;
        }
        Sighting sighting = evidence.getSighting();
        if (sighting != null) {
            sighting.getEvidences().remove(evidence);
            return;
        }
        evidences.delete(evidence);
    }

    /** shared validation, also used when evidences are sent inside a sighting or claim body */
    public static void validate(Evidence e) {
        if (e == null) {
            throw new IllegalArgumentException("evidence cannot be null");
        }
        if (e.getType() == null || e.getType().isBlank()) {
            throw new IllegalArgumentException("evidence type is required (e.g. PHOTO, VET_RECORD, MICROCHIP)");
        }
        if (e.getFileUrl() == null || e.getFileUrl().isBlank()) {
            throw new IllegalArgumentException("evidence fileUrl is required");
        }
        if (e.getScore() != null && (e.getScore() < 0 || e.getScore() > 1)) {
            throw new IllegalArgumentException("evidence score must be between 0 and 1");
        }
    }
}
