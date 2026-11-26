package com.pawradise.lostandfoundservice.controller;

import com.pawradise.lostandfoundservice.dto.NearbyReport;
import com.pawradise.lostandfoundservice.entity.Report;
import com.pawradise.lostandfoundservice.service.ReportService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Endpoints shared by lost and found reports: list, search, nearby, status.
 * Sightings -> SightingController, claims -> OwnershipClaimController.
 */
@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService service;

    public ReportController(ReportService service) {
        this.service = service;
    }

    @GetMapping
    public List<Report> findAll() {
        return service.findAll();
    }

    // GET /api/reports/search?type=LOST&animalType=dog&color=black&city=tunis&status=OPEN -> 200, 400
    @GetMapping("/search")
    public List<Report> search(@RequestParam(required = false) String type,
                               @RequestParam(required = false) String animalType,
                               @RequestParam(required = false) String breed,
                               @RequestParam(required = false) String color,
                               @RequestParam(required = false) String size,
                               @RequestParam(required = false) String city,
                               @RequestParam(required = false) String status) {
        return service.search(type, animalType, breed, color, size, city, status);
    }

    // GET /api/reports/nearby?lat=36.8&lng=10.18&radiusKm=5&type=FOUND -> 200, 400
    @GetMapping("/nearby")
    public List<NearbyReport> nearby(@RequestParam(required = false) Double lat,
                                     @RequestParam(required = false) Double lng,
                                     @RequestParam(required = false) Double radiusKm,
                                     @RequestParam(required = false) String type) {
        return service.nearby(lat, lng, radiusKm, type);
    }

    @GetMapping("/{id}")
    public Report findById(@PathVariable Long id) {
        return service.findById(id);
    }

    // PUT /api/reports/{id}/status  body: {"status":"RECOVERED"} -> 200, 400, 404, 409
    @PutMapping("/{id}/status")
    public Report updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return service.updateStatus(id, body.get("status"));
    }
}
