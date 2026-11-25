package tn.esprit.adoption.event;

import java.time.LocalDate;

public record AdoptionCompletedEvent(
        Long annonceId,
        Long candidatureId,
        String idAnimal,
        String idRefuge,
        String idCandidat,
        LocalDate dateAdoption
) {
}