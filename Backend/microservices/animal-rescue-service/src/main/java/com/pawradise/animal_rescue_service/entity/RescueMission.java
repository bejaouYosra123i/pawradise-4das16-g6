package com.pawradise.animal_rescue_service.entity;

import com.pawradise.animal_rescue_service.enums.MissionOutcome;
import com.pawradise.animal_rescue_service.enums.MissionStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rescue_missions")
@Getter
@Setter
@NoArgsConstructor
public class RescueMission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String missionCode;

    @Enumerated(EnumType.STRING)
    private MissionStatus status = MissionStatus.PROPOSED;

    private LocalDateTime createdAt;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;

    private String animalCondition;

    @Enumerated(EnumType.STRING)
    private MissionOutcome outcome;

    private Boolean readyForAdoption = false;

    @Version
    private Long version;

    // 1 demande -> 0..1 mission
    @OneToOne(optional = false)
    @JoinColumn(name = "request_id", unique = true)
    private RescueRequest request;

    // 1 équipe réalise 0..* missions
    @ManyToOne
    @JoinColumn(name = "team_id")
    private RescueTeam team;

    // 1 mission nécessite 0..* ressources
    @OneToMany(mappedBy = "mission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ResourceRequirement> resources = new ArrayList<>();

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}