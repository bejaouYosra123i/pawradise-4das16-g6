package tn.esprit.adoption.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
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
    @Column(nullable = false)
    private StatutCandidature statut = StatutCandidature.EN_ATTENTE;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "annonce_id")
    @JsonIgnore
    private AnnonceAdoption annonce;

    @JsonIgnore
    @OneToMany(mappedBy = "candidature", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Evaluation> evaluations = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "candidature", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rencontre> rencontres = new ArrayList<>();

    @JsonIgnore
    @OneToOne(mappedBy = "candidature", cascade = CascadeType.ALL, orphanRemoval = true)
    private ContratAdoption contrat;

    @PrePersist
    void prePersist() {
        if (statut == null) {
            statut = StatutCandidature.EN_ATTENTE;
        }
        if (dateDepot == null) {
            dateDepot = LocalDate.now();
        }
    }
}