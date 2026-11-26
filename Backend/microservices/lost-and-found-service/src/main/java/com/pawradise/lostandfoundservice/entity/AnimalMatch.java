package com.pawradise.lostandfoundservice.entity;

import com.pawradise.lostandfoundservice.enums.MatchStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/** UML: LostReport 1 --- 0..* AnimalMatch 0..* --- 1 FoundReport */
@Entity
@Table(name = "animal_matches",
        uniqueConstraints = @UniqueConstraint(columnNames = {"lost_report_id", "found_report_id"}))
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

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "lost_report_id")
    private LostReport lostReport;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "found_report_id")
    private FoundReport foundReport;

    public AnimalMatch(LostReport lostReport, FoundReport foundReport, Double score) {
        this.lostReport = lostReport;
        this.foundReport = foundReport;
        this.score = score;
        this.status = MatchStatus.SUGGESTED;
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }

    /** UML: confirm() */
    public void confirm() {
        requireSuggested();
        this.status = MatchStatus.CONFIRMED;
    }

    public void reject() {
        requireSuggested();
        this.status = MatchStatus.REJECTED;
    }

    private void requireSuggested() {
        if (status != MatchStatus.SUGGESTED) {
            throw new IllegalStateException("Match " + id + " is already " + status);
        }
    }
}
