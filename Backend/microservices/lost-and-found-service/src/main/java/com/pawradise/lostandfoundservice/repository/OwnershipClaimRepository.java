package com.pawradise.lostandfoundservice.repository;

import com.pawradise.lostandfoundservice.entity.OwnershipClaim;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OwnershipClaimRepository extends JpaRepository<OwnershipClaim, Long> {
    List<OwnershipClaim> findByReportId(Long reportId);
    void deleteByReportId(Long reportId);
}