package com.pawradise.lostandfoundservice.repository;

import com.pawradise.lostandfoundservice.entity.AnimalMatch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnimalMatchRepository extends JpaRepository<AnimalMatch, Long> {
    List<AnimalMatch> findByLostReport_IdOrFoundReport_IdOrderByScoreDesc(Long lostReportId, Long foundReportId);
    List<AnimalMatch> findByLostReport_IdOrFoundReport_Id(Long lostReportId, Long foundReportId);
    boolean existsByLostReport_IdAndFoundReport_Id(Long lostReportId, Long foundReportId);
}
