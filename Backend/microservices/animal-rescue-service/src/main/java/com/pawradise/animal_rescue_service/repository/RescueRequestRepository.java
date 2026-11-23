package com.pawradise.animal_rescue_service.repository;

import com.pawradise.animal_rescue_service.entity.RescueRequest;
import com.pawradise.animal_rescue_service.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RescueRequestRepository extends JpaRepository<RescueRequest, Long> {

    List<RescueRequest> findByStatus(RequestStatus status);
}