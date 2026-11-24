package com.pawradise.lostandfoundservice.controller;

import com.pawradise.lostandfoundservice.entity.FoundReport;
import com.pawradise.lostandfoundservice.service.FoundReportService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/found-reports")
public class FoundReportController {

    private final FoundReportService service;

    public FoundReportController(FoundReportService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FoundReport create(@RequestBody FoundReport report) {
        return service.create(report);
    }

    @GetMapping
    public List<FoundReport> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public FoundReport findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public FoundReport update(@PathVariable Long id, @RequestBody FoundReport report) {
        return service.update(id, report);
    }

    // POST /api/found-reports/{id}/rescue -> 200, 404, 409 (not injured / already requested)
    @PostMapping("/{id}/rescue")
    public FoundReport requestRescue(@PathVariable Long id) {
        return service.requestRescue(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}