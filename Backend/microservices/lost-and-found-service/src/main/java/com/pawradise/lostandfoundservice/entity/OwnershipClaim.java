package com.pawradise.lostandfoundservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pawradise.lostandfoundservice.enums.ClaimStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** UML: Report 1 --- 0..* OwnershipClaim, OwnershipClaim 1 <>--- 1..* Evidence (composition). */
@Entity
@Table(name = "ownership_claims")
@Getter
@Setter
@NoArgsConstructor
public class OwnershipClaim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String claimantId;
    private Double latitude;
    private Double longitude;

    @Column(length = 1000)
    private String message;

    @Enumerated(EnumType.STRING)
    private ClaimStatus status = ClaimStatus.PENDING;

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "report_id")
    @JsonIgnore
    private Report report;

    // composition: a claim must always keep at least one evidence (1..*)
    @OneToMany(mappedBy = "claim", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Evidence> evidences = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (status == null) status = ClaimStatus.PENDING;
    }

    @JsonProperty(value = "reportId", access = JsonProperty.Access.READ_ONLY)
    public Long getReportId() {
        return report != null ? report.getId() : null;
    }

    public void addEvidence(Evidence evidence) {
        evidence.setClaim(this);
        evidence.setSighting(null);
        evidences.add(evidence);
    }

    /** UML: accept() */
    public void accept() {
        requirePending();
        this.status = ClaimStatus.ACCEPTED;
    }

    public void reject() {
        requirePending();
        this.status = ClaimStatus.REJECTED;
    }

    public void requirePending() {
        if (status != ClaimStatus.PENDING) {
            throw new IllegalStateException("Claim " + id + " is already " + status);
        }
    }
}
