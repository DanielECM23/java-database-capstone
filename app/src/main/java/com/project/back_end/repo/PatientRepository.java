package com.project.back_end.repo;

import com.project.back_end.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    // Buscar paciente por email
    Optional<Patient> findByEmail(String email);

    // Buscar paciente por email o teléfono
    Optional<Patient> findByEmailOrPhone(String email, String phone);

    // Verificar si existe un paciente por email
    boolean existsByEmail(String email);
}
