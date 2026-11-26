package com.pawradise.lostandfoundservice.repository;

import com.pawradise.lostandfoundservice.entity.Sighting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SightingRepository extends JpaRepository<Sighting, Long> {
    List<Sighting> findByReport_IdOrderBySeenAtDesc(Long reportId);
    List<Sighting> findByReport_Id(Long reportId);
}
