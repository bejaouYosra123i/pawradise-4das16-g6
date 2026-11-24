package com.pawradise.lostandfoundservice.repository;

import com.pawradise.lostandfoundservice.entity.Evidence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EvidenceRepository extends JpaRepository<Evidence, Long> {
    List<Evidence> findBySighting_Id(Long sightingId);
    List<Evidence> findByClaim_Id(Long claimId);
}
