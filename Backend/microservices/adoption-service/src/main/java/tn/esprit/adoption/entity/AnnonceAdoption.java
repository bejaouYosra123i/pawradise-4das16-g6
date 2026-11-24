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
public class AnnonceAdoption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String idAnimal;

    @NotBlank
    private String idRefuge;

    @Column(length = 2000)
    private String description;

    private LocalDate dateAnnonce;

    @Column(nullable = false)
    private Boolean disponible = true;

    @JsonIgnore
    @OneToMany(mappedBy = "annonce", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Candidature> candidatures = new ArrayList<>();

    @PrePersist
    void prePersist() {
        if (disponible == null) {
            disponible = true;
        }
        if (dateAnnonce == null) {
            dateAnnonce = LocalDate.now();
        }
    }
}