package com.pawradise.lostandfoundservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** UML: Report 1 --- 0..* Sighting, Sighting 1 <>--- 0..* Evidence (composition). */
@Entity
@Table(name = "sightings")
@Getter
@Setter
@NoArgsConstructor
public class Sighting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String observerId;
    private LocalDateTime seenAt;
    private Double latitude;
    private Double longitude;
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "report_id")
    @JsonIgnore
    private Report report;

    // composition: deleting a sighting deletes its evidences
    @OneToMany(mappedBy = "sighting", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Evidence> evidences = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (seenAt == null) seenAt = LocalDateTime.now();
    }

    @JsonProperty(value = "reportId", access = JsonProperty.Access.READ_ONLY)
    public Long getReportId() {
        return report != null ? report.getId() : null;
    }

    public void addEvidence(Evidence evidence) {
        evidence.setSighting(this);
        evidence.setClaim(null);
        evidences.add(evidence);
    }
}
