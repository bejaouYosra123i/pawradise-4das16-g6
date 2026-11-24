package tn.esprit.adoption.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Candidature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String idCandidat;

    @Column(length = 2000)
    private String message;

    private LocalDate dateDepot;

    @Enumerated(EnumType.STRING)
    private StatutCandidature statut = StatutCandidature.EN_ATTENTE;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "annonce_id")
    @JsonIgnore
    private AnnonceAdoption annonce;

    @OneToMany(mappedBy = "candidature", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Evaluation> evaluations = new ArrayList<>();

    @OneToMany(mappedBy = "candidature", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rencontre> rencontres = new ArrayList<>();

    @OneToOne(mappedBy = "candidature", cascade = CascadeType.ALL, orphanRemoval = true)
    private ContratAdoption contrat;
}