package com.pawradise.lostandfoundservice.repository;

import com.pawradise.lostandfoundservice.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}