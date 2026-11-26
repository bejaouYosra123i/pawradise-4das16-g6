package com.pawradise.lostandfoundservice.controller;

import com.pawradise.lostandfoundservice.entity.OwnershipClaim;
import com.pawradise.lostandfoundservice.service.OwnershipClaimService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OwnershipClaimController {

    private final OwnershipClaimService service;

    public OwnershipClaimController(OwnershipClaimService service) {
        this.service = service;
    }

    // POST /api/reports/{id}/claims -> 201, 400, 404, 409
    @PostMapping("/reports/{reportId}/claims")
    @ResponseStatus(HttpStatus.CREATED)
    public OwnershipClaim create(@PathVariable Long reportId, @RequestBody OwnershipClaim claim) {
        return service.create(reportId, claim);
    }

    @GetMapping("/reports/{reportId}/claims")
    public List<OwnershipClaim> findByReport(@PathVariable Long reportId) {
        return service.findByReport(reportId);
    }

    @GetMapping("/claims/{id}")
    public OwnershipClaim findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/claims/{id}")
    public OwnershipClaim update(@PathVariable Long id, @RequestBody OwnershipClaim claim) {
        return service.update(id, claim);
    }

    @DeleteMapping("/claims/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @PutMapping("/claims/{id}/accept")
    public OwnershipClaim accept(@PathVariable Long id) {
        return service.accept(id);
    }

    @PutMapping("/claims/{id}/reject")
    public OwnershipClaim reject(@PathVariable Long id) {
        return service.reject(id);
    }
}
