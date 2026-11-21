package com.pawradise.lostandfoundservice.repository;

import com.pawradise.lostandfoundservice.entity.LostReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LostReportRepository extends JpaRepository<LostReport, Long> {
}