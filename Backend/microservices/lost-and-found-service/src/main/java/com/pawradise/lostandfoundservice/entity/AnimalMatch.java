package com.pawradise.lostandfoundservice.entity;

import com.pawradise.lostandfoundservice.enums.MatchStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "animal_matches")
@Getter
@Setter
@NoArgsConstructor
public class AnimalMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double score;

    @Enumerated(EnumType.STRING)
    private MatchStatus status = MatchStatus.SUGGESTED;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lost_report_id")
    private LostReport lostReport;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "found_report_id")
    private FoundReport foundReport;

    public void confirm() {
        this.status = MatchStatus.CONFIRMED;
    }
}