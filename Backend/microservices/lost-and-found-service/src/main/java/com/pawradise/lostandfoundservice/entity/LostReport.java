package com.pawradise.lostandfoundservice.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("LOST")
@Getter
@Setter
@NoArgsConstructor
public class LostReport extends Report {

    private String petName;
    private LocalDate lostDate;
}