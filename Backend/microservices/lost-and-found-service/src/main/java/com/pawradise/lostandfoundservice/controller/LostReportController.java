package com.pawradise.lostandfoundservice.controller;

import com.pawradise.lostandfoundservice.entity.LostReport;
import com.pawradise.lostandfoundservice.service.LostReportService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lost-reports")
public class LostReportController {

    private final LostReportService service;

    public LostReportController(LostReportService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LostReport create(@RequestBody LostReport report) {
        return service.create(report);
    }

    @GetMapping
    public List<LostReport> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public LostReport findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public LostReport update(@PathVariable Long id, @RequestBody LostReport report) {
        return service.update(id, report);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}