package com.pawradise.vetcare.repositories;

import com.pawradise.vetcare.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByAnimalId(Long animalId);
}
