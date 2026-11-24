package com.pawradise.lostandfoundservice.repository;

import com.pawradise.lostandfoundservice.entity.LostReport;
import com.pawradise.lostandfoundservice.enums.CaseStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LostReportRepository extends JpaRepository<LostReport, Long> {
    List<LostReport> findByAnimalTypeIgnoreCaseAndStatus(String animalType, CaseStatus status);
}
