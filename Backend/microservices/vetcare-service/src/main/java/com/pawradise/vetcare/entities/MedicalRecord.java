package com.pawradise.vetcare.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MedicalRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private Long animalId;                     // external ref (shared id)
    private String bloodType;
    @Column(length = 1000) private String allergies;
    @Column(length = 1000) private String chronicConditions;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdated;

    @PrePersist
    void onCreate() { createdAt = LocalDateTime.now(); lastUpdated = createdAt; }

    @PreUpdate
    void onUpdate() { lastUpdated = LocalDateTime.now(); }
}
