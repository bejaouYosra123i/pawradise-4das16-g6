package tn.esprit.adoption.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ContratAdoption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dateSignature;

    @Column(length = 2000)
    private String conditions;

    private boolean signe;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "candidature_id", unique = true)
    @JsonIgnore
    private Candidature candidature;

    @OneToMany(mappedBy = "contrat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SuiviPostAdoption> suivis = new ArrayList<>();
}