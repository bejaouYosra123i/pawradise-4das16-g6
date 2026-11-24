package com.pawradise.lostandfoundservice.service;

import com.pawradise.lostandfoundservice.entity.AnimalMatch;
import com.pawradise.lostandfoundservice.entity.FoundReport;
import com.pawradise.lostandfoundservice.entity.LostReport;
import com.pawradise.lostandfoundservice.entity.Report;
import com.pawradise.lostandfoundservice.enums.CaseStatus;
import com.pawradise.lostandfoundservice.enums.MatchStatus;
import com.pawradise.lostandfoundservice.exception.NotFoundException;
import com.pawradise.lostandfoundservice.repository.AnimalMatchRepository;
import com.pawradise.lostandfoundservice.repository.FoundReportRepository;
import com.pawradise.lostandfoundservice.repository.LostReportRepository;
import com.pawradise.lostandfoundservice.repository.ReportRepository;
import com.pawradise.lostandfoundservice.util.Coordinates;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Compares a lost report with the open found reports (or the opposite) and stores
 * the likely pairs as AnimalMatch (status SUGGESTED). A person then confirms or rejects them.
 */
@Service
public class AnimalMatchService {

    /** below this score a pair is not suggested */
    static final double MIN_SCORE = 0.4;

    private final AnimalMatchRepository matches;
    private final ReportRepository reports;
    private final LostReportRepository lostReports;
    private final FoundReportRepository foundReports;

    public AnimalMatchService(AnimalMatchRepository matches, ReportRepository reports,
                              LostReportRepository lostReports, FoundReportRepository foundReports) {
        this.matches = matches;
        this.reports = reports;
        this.lostReports = lostReports;
        this.foundReports = foundReports;
    }

    /**
     * GET /api/matches/{reportId} -> 200 (list), 204 (no match), 404 (unknown report).
     * Computes new suggestions if the report is still OPEN, then returns all its matches, best first.
     */
    @Transactional
    public List<AnimalMatch> findForReport(Long reportId) {
        Report report = reports.findById(reportId)
                .orElseThrow(() -> new NotFoundException("Report " + reportId + " not found"));

        if (report.getStatus() == CaseStatus.OPEN && report.getAnimalType() != null) {
            if (report instanceof LostReport lostReport) {
                for (FoundReport candidate : foundReports.findByAnimalTypeIgnoreCaseAndStatus(lostReport.getAnimalType(), CaseStatus.OPEN)) {
                    suggestIfGood(lostReport, candidate);
                }
            } else if (report instanceof FoundReport foundReport) {
                for (LostReport candidate : lostReports.findByAnimalTypeIgnoreCaseAndStatus(foundReport.getAnimalType(), CaseStatus.OPEN)) {
                    suggestIfGood(candidate, foundReport);
                }
            }
        }
        return matches.findByLostReport_IdOrFoundReport_IdOrderByScoreDesc(reportId, reportId);
    }

    @Transactional(readOnly = true)
    public AnimalMatch findById(Long id) {
        return matches.findById(id)
                .orElseThrow(() -> new NotFoundException("Match " + id + " not found"));
    }

    /** Confirming a match marks both reports MATCHED and rejects their other suggestions. */
    @Transactional
    public AnimalMatch confirm(Long id) {
        AnimalMatch match = findById(id);
        LostReport lost = match.getLostReport();
        FoundReport found = match.getFoundReport();
        if (lost.isFinished() || found.isFinished()) {
            throw new IllegalStateException("One of the reports is already closed or recovered");
        }
        match.confirm();
        if (lost.getStatus() == CaseStatus.OPEN) lost.setStatus(CaseStatus.MATCHED);
        if (found.getStatus() == CaseStatus.OPEN) found.setStatus(CaseStatus.MATCHED);
        reports.save(lost);
        reports.save(found);

        rejectOtherSuggestions(lost.getId(), match.getId());
        rejectOtherSuggestions(found.getId(), match.getId());
        return matches.save(match);
    }

    @Transactional
    public AnimalMatch reject(Long id) {
        AnimalMatch match = findById(id);
        match.reject();
        return matches.save(match);
    }

    @Transactional
    public void delete(Long id) {
        matches.delete(findById(id));
    }

    // ------------------------------------------------------------------

    private void suggestIfGood(LostReport lost, FoundReport found) {
        if (matches.existsByLostReport_IdAndFoundReport_Id(lost.getId(), found.getId())) {
            return; // already suggested / decided before
        }
        double score = score(lost, found);
        if (score >= MIN_SCORE) {
            matches.save(new AnimalMatch(lost, found, score));
        }
    }

    private void rejectOtherSuggestions(Long reportId, Long keptMatchId) {
        for (AnimalMatch m : matches.findByLostReport_IdOrFoundReport_Id(reportId, reportId)) {
            if (!m.getId().equals(keptMatchId) && m.getStatus() == MatchStatus.SUGGESTED) {
                m.reject();
            }
        }
    }

    /**
     * Simple similarity score between 0 and 1:
     * same animal type (required) 0.2, breed 0.2, color 0.2, size 0.1, city 0.1, distance up to 0.2.
     * An animal cannot be found before it was lost -> 0.
     */
    static double score(LostReport lost, FoundReport found) {
        if (!same(lost.getAnimalType(), found.getAnimalType())) {
            return 0;
        }
        if (lost.getLostDate() != null && found.getFoundDate() != null
                && found.getFoundDate().isBefore(lost.getLostDate())) {
            return 0;
        }
        double s = 0.2;
        if (same(lost.getBreed(), found.getBreed())) s += 0.2;
        if (same(lost.getColor(), found.getColor())) s += 0.2;
        if (same(lost.getSize(), found.getSize())) s += 0.1;
        if (same(lost.getCity(), found.getCity())) s += 0.1;

        if (lost.getLatitude() != null && lost.getLongitude() != null
                && found.getLatitude() != null && found.getLongitude() != null) {
            double km = Coordinates.distanceKm(lost.getLatitude(), lost.getLongitude(),
                    found.getLatitude(), found.getLongitude());
            if (km <= 2) s += 0.2;
            else if (km <= 10) s += 0.1;
        }
        return Math.round(s * 100.0) / 100.0;
    }

    private static boolean same(String a, String b) {
        return a != null && b != null && !a.isBlank() && a.trim().equalsIgnoreCase(b.trim());
    }
}
