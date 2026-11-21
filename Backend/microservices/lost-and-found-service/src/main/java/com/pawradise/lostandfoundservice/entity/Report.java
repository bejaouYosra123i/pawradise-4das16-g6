package com.pawradise.lostandfoundservice.entity;

import com.pawradise.lostandfoundservice.enums.CaseStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "report_type")
@Getter
@Setter
@NoArgsConstructor
public abstract class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reporterId;
    private String animalType;
    private String breed;
    private String color;
    private String size;
    private String distinctiveMarks;

    @Column(length = 1000)
    private String description;

    private String photoUrl;
    private String city;
    private Double latitude;
    private Double longitude;

    @Enumerated(EnumType.STRING)
    private CaseStatus status;

    private LocalDateTime openedAt;
    private LocalDateTime closedAt;

    private Long adoptionListingId;

    @PrePersist
    protected void onCreate() {
        if (status == null) status = CaseStatus.OPEN;
        if (openedAt == null) openedAt = LocalDateTime.now();
    }

    public void close() {
        this.status = CaseStatus.CLOSED;
        this.closedAt = LocalDateTime.now();
    }
}