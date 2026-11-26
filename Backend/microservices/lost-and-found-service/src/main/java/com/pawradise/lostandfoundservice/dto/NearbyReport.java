package com.pawradise.lostandfoundservice.dto;

import com.pawradise.lostandfoundservice.entity.Report;

/** One result of GET /api/reports/nearby: the report and its distance from the searched point. */
public record NearbyReport(Report report, double distanceKm) {
}
