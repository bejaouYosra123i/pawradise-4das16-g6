package com.pawradise.lostandfoundservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * UML: Evidence (photo, document, vet record...) that justifies a Sighting or an OwnershipClaim.
 * Exactly one of sighting / claim is set.
 */
@Entity
@Table(name = "evidences")
@Getter
@Setter
@NoArgsConstructor
public class Evidence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** reliability of the evidence, between 0 and 1 (optional) */
    private Double score;

    /** e.g. PHOTO, VIDEO, VET_RECORD, MICROCHIP, INVOICE */
    private String type;

    private String fileUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sighting_id")
    @JsonIgnore
    private Sighting sighting;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claim_id")
    @JsonIgnore
    private OwnershipClaim claim;

    @JsonProperty(value = "sightingId", access = JsonProperty.Access.READ_ONLY)
    public Long getSightingId() {
        return sighting != null ? sighting.getId() : null;
    }

    @JsonProperty(value = "claimId", access = JsonProperty.Access.READ_ONLY)
    public Long getClaimId() {
        return claim != null ? claim.getId() : null;
    }
}
