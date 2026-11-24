package com.pawradise.lostandfoundservice.repository;

import com.pawradise.lostandfoundservice.entity.OwnershipClaim;
import com.pawradise.lostandfoundservice.enums.ClaimStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OwnershipClaimRepository extends JpaRepository<OwnershipClaim, Long> {
    List<OwnershipClaim> findByReport_Id(Long reportId);
    List<OwnershipClaim> findByReport_IdAndStatus(Long reportId, ClaimStatus status);
    boolean existsByReport_IdAndClaimantIdAndStatus(Long reportId, String claimantId, ClaimStatus status);
}
