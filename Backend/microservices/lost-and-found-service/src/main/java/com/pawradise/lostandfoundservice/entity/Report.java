package com.pawradise.lostandfoundservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pawradise.lostandfoundservice.enums.CaseStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Abstract parent of LostReport and FoundReport (UML: Report).
 * The SearchCase lifecycle (status, openedAt, closedAt, close()) is stored directly on the report.
 */
@Entity
@Table(name = "reports")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "report_type")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Getter
@Setter
@NoArgsConstructor
public abstract class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reporterId;
    private String animalType;
    private String breed;
    private String color;
    private String size;
    private String distinctiveMarks;

    @Column(length = 1000)
    private String description;

    private String photoUrl;
    private String city;
    private Double latitude;
    private Double longitude;

    @Enumerated(EnumType.STRING)
    private CaseStatus status;

    private LocalDateTime openedAt;
    private LocalDateTime closedAt;

    private Long adoptionListingId;

    @PrePersist
    protected void onCreate() {
        if (status == null) status = CaseStatus.OPEN;
        if (openedAt == null) openedAt = LocalDateTime.now();
    }

    public void close() {
        this.status = CaseStatus.CLOSED;
        this.closedAt = LocalDateTime.now();
    }

    /** true when the case is finished and must not change anymore. */
    @JsonIgnore
    public boolean isFinished() {
        return status == CaseStatus.CLOSED || status == CaseStatus.RECOVERED;
    }

    /** "LOST" or "FOUND", shown in JSON so clients know which kind of report they got. */
    @JsonProperty(value = "reportType", access = JsonProperty.Access.READ_ONLY)
    public String getReportType() {
        return (this instanceof LostReport) ? "LOST" : "FOUND";
    }
}
