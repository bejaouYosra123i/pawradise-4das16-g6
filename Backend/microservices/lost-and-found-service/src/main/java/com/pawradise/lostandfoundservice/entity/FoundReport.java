package com.pawradise.lostandfoundservice.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("FOUND")
@Getter
@Setter
@NoArgsConstructor
public class FoundReport extends Report {

    private LocalDate foundDate;
    private Boolean injured = false;
    private Boolean rescueReferralSent = false;

    /** UML: requestRescue(). Only an injured animal can be referred to Animal Rescue, and only once. */
    public void requestRescue() {
        if (!Boolean.TRUE.equals(injured)) {
            throw new IllegalStateException("Only an injured animal can be referred to Animal Rescue");
        }
        if (Boolean.TRUE.equals(rescueReferralSent)) {
            throw new IllegalStateException("Rescue was already requested for this report");
        }
        if (isFinished()) {
            throw new IllegalStateException("Report is already " + getStatus());
        }
        this.rescueReferralSent = true;
    }
}
