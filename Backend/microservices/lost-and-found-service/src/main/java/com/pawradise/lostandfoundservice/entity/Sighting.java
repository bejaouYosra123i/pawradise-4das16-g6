package com.pawradise.lostandfoundservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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

    @PrePersist
    protected void onCreate() {
        if (seenAt == null) seenAt = LocalDateTime.now();
    }
}