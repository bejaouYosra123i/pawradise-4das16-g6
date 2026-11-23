package com.pawradise.animal_rescue_service.entity;

import com.pawradise.animal_rescue_service.enums.RequestStatus;
import com.pawradise.animal_rescue_service.enums.UrgencyLevel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "rescue_requests")
@Getter
@Setter
@NoArgsConstructor
public class RescueRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long animalId;

    @Column(nullable = false)
    private String animalType;

    @Column(nullable = false)
    private String reporterName;

    private String reporterPhone;

    @Column(nullable = false, length = 1000)
    private String description;

    private String photoUrl;

    @Column(nullable = false)
    private String locationAddress;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UrgencyLevel urgencyLevel;

    private Integer urgencyScore;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status;

    private LocalDateTime reportedAt;

    // Triage : calcule le score d'urgence à partir du niveau
    public void triage() {
        if (urgencyLevel == null) {
            throw new IllegalStateException("Le niveau d'urgence est obligatoire pour le triage");
        }
        this.urgencyScore = switch (urgencyLevel) {
            case LOW -> 10;
            case MEDIUM -> 40;
            case HIGH -> 70;
            case CRITICAL -> 100;
        };
    }

    @PrePersist
    void onCreate() {
        this.reportedAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = RequestStatus.PENDING;
        }
        triage();
    }
}