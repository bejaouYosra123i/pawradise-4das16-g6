package com.pawradise.lostandfoundservice.repository;

import com.pawradise.lostandfoundservice.entity.FoundReport;
import com.pawradise.lostandfoundservice.enums.CaseStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoundReportRepository extends JpaRepository<FoundReport, Long> {
    List<FoundReport> findByAnimalTypeIgnoreCaseAndStatus(String animalType, CaseStatus status);
}
