package com.pawradise.animal_rescue_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pawradise.animal_rescue_service.enums.ResourceStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "resource_requirements")
@Getter
@Setter
@NoArgsConstructor
public class ResourceRequirement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemName;

    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private ResourceStatus status = ResourceStatus.REQUESTED;

    @ManyToOne(optional = false)
    @JoinColumn(name = "mission_id")
    @JsonIgnore
    private RescueMission mission;
}