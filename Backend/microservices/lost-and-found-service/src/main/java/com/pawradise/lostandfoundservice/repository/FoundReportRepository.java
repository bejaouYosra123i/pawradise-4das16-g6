package com.pawradise.lostandfoundservice.repository;

import com.pawradise.lostandfoundservice.entity.FoundReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoundReportRepository extends JpaRepository<FoundReport, Long> {
}