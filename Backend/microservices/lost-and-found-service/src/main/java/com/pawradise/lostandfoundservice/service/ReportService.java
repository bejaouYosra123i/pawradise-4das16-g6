package com.pawradise.lostandfoundservice.service;

import com.pawradise.lostandfoundservice.dto.NearbyReport;
import com.pawradise.lostandfoundservice.entity.FoundReport;
import com.pawradise.lostandfoundservice.entity.LostReport;
import com.pawradise.lostandfoundservice.entity.Report;
import com.pawradise.lostandfoundservice.enums.CaseStatus;
import com.pawradise.lostandfoundservice.exception.NotFoundException;
import com.pawradise.lostandfoundservice.repository.ReportRepository;
import com.pawradise.lostandfoundservice.util.Coordinates;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** Operations common to lost AND found reports: list, search, nearby, status. */
@Service
public class ReportService {

    private static final double MAX_RADIUS_KM = 100;

    private final ReportRepository reports;
    private final EntityManager em;

    public ReportService(ReportRepository reports, EntityManager em) {
        this.reports = reports;
        this.em = em;
    }

    @Transactional(readOnly = true)
    public List<Report> findAll() {
        return reports.findAll();
    }

    @Transactional(readOnly = true)
    public Report findById(Long id) {
        return reports.findById(id)
                .orElseThrow(() -> new NotFoundException("Report " + id + " not found"));
    }

    /**
     * GET /api/reports/search -> 200, 400
     * Every parameter is optional. Text filters are case-insensitive "contains".
     */
    @Transactional(readOnly = true)
    public List<Report> search(String type, String animalType, String breed, String color,
                               String size, String city, String status) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Report> q = cb.createQuery(Report.class);
        Root<Report> r = q.from(Report.class);
        List<Predicate> where = new ArrayList<>();

        if (notBlank(type)) {
            Class<? extends Report> cls = parseType(type);
            where.add(cb.equal(r.type(), cls));
        }
        if (notBlank(status)) {
            where.add(cb.equal(r.get("status"), parseStatus(status)));
        }
        addContains(cb, r, where, "animalType", animalType);
        addContains(cb, r, where, "breed", breed);
        addContains(cb, r, where, "color", color);
        addContains(cb, r, where, "size", size);
        addContains(cb, r, where, "city", city);

        q.select(r).where(where.toArray(new Predicate[0])).orderBy(cb.desc(r.get("openedAt")));
        return em.createQuery(q).getResultList();
    }

    /**
     * GET /api/reports/nearby?lat=..&lng=..&radiusKm=5[&type=LOST|FOUND] -> 200, 400
     * Returns the reports that are still active (not CLOSED / RECOVERED) within the radius, nearest first.
     */
    @Transactional(readOnly = true)
    public List<NearbyReport> nearby(Double lat, Double lng, Double radiusKm, String type) {
        if (lat == null || lng == null) {
            throw new IllegalArgumentException("lat and lng are required");
        }
        Coordinates.validate(lat, lng);
        double radius = radiusKm == null ? 5 : radiusKm;
        if (radius <= 0 || radius > MAX_RADIUS_KM) {
            throw new IllegalArgumentException("radiusKm must be > 0 and <= " + (int) MAX_RADIUS_KM);
        }
        Class<? extends Report> cls = notBlank(type) ? parseType(type) : null;

        // bounding box first (cheap SQL), exact distance afterwards
        double dLat = radius / 111.0;
        double cos = Math.max(Math.cos(Math.toRadians(lat)), 0.01);
        double dLng = radius / (111.0 * cos);

        return reports.findByLatitudeBetweenAndLongitudeBetween(lat - dLat, lat + dLat, lng - dLng, lng + dLng)
                .stream()
                .filter(rep -> !rep.isFinished())
                .filter(rep -> cls == null || cls.isInstance(rep))
                .map(rep -> new NearbyReport(rep,
                        round2(Coordinates.distanceKm(lat, lng, rep.getLatitude(), rep.getLongitude()))))
                .filter(n -> n.distanceKm() <= radius)
                .sorted(Comparator.comparingDouble(NearbyReport::distanceKm))
                .toList();
    }

    /** PUT /api/reports/{id}/status  body {"status":"RECOVERED"} -> 200, 400, 404, 409 */
    @Transactional
    public Report updateStatus(Long id, String statusValue) {
        Report report = findById(id);
        if (!notBlank(statusValue)) {
            throw new IllegalArgumentException("status is required");
        }
        CaseStatus newStatus = parseStatus(statusValue);
        if (report.getStatus() == CaseStatus.CLOSED) {
            throw new IllegalStateException("Report " + id + " is already CLOSED");
        }
        if (newStatus == report.getStatus()) {
            throw new IllegalStateException("Report " + id + " is already " + newStatus);
        }
        if (newStatus == CaseStatus.CLOSED || newStatus == CaseStatus.RECOVERED) {
            report.close();               // sets closedAt
            report.setStatus(newStatus);  // keep RECOVERED if that was asked
        } else {
            report.setStatus(newStatus);
        }
        return reports.save(report);
    }

    // ------------------------------------------------------------------

    private static void addContains(CriteriaBuilder cb, Root<Report> r, List<Predicate> where, String field, String value) {
        if (notBlank(value)) {
            where.add(cb.like(cb.lower(r.<String>get(field)), "%" + value.trim().toLowerCase() + "%"));
        }
    }

    private static Class<? extends Report> parseType(String type) {
        return switch (type.trim().toUpperCase()) {
            case "LOST" -> LostReport.class;
            case "FOUND" -> FoundReport.class;
            default -> throw new IllegalArgumentException("Invalid type. Allowed: LOST, FOUND");
        };
    }

    private static CaseStatus parseStatus(String status) {
        try {
            return CaseStatus.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status. Allowed: OPEN, MATCHED, CLAIMED, RECOVERED, CLOSED");
        }
    }

    private static boolean notBlank(String s) {
        return s != null && !s.isBlank();
    }

    private static double round2(double v) {
        return Math.round(v * 100.0) / 100.0;
    }
}
