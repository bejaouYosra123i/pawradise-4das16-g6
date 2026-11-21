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
}