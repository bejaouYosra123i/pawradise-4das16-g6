package com.pawradise.lostandfoundservice.repository;

import com.pawradise.lostandfoundservice.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    /** bounding box pre-filter for /nearby, the exact distance is computed in Java */
    List<Report> findByLatitudeBetweenAndLongitudeBetween(Double minLat, Double maxLat, Double minLng, Double maxLng);
}
