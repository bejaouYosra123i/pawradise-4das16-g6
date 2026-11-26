package com.pawradise.vetcare.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Vaccination {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String vaccineName;
    private String vaccineType;
    private String batchNumber;
    private LocalDate administeredAt;
    private LocalDate nextDueDate;

    @ManyToOne(optional = false)
    private MedicalRecord medicalRecord;
}
