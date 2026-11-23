package com.pawradise.animal_rescue_service.entity;

import com.pawradise.animal_rescue_service.enums.TeamStatus;
import com.pawradise.animal_rescue_service.enums.TeamType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "rescue_teams")
@Getter
@Setter
@NoArgsConstructor
public class RescueTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @Enumerated(EnumType.STRING)
    private TeamType type;

    @Enumerated(EnumType.STRING)
    private TeamStatus status = TeamStatus.AVAILABLE;

    private String description;

    private String baseZone;

    @Version
    private Long version;

    public boolean isAvailable() {
        return status == TeamStatus.AVAILABLE;
    }
}