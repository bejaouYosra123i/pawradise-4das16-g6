package tn.esprit.marketplace.messaging;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AdoptionCompletedEvent(
        String annonceId, String candidatureId, String idAnimal,
        String idRefuge, String idCandidat, String dateAdoption) {}