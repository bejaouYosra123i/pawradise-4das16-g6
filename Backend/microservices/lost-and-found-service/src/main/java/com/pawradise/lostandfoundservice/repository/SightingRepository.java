package com.pawradise.lostandfoundservice.repository;

import com.pawradise.lostandfoundservice.entity.Sighting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SightingRepository extends JpaRepository<Sighting, Long> {
    List<Sighting> findByReportId(Long reportId);
    void deleteByReportId(Long reportId);
}