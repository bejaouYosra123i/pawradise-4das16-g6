package com.pawradise.lostandfoundservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pawradise.lostandfoundservice.enums.ClaimStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    private String proofUrl;

    @Enumerated(EnumType.STRING)
    private ClaimStatus status = ClaimStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "report_id")
    @JsonIgnore
    private Report report;

    public void accept() {
        this.status = ClaimStatus.ACCEPTED;
    }
}