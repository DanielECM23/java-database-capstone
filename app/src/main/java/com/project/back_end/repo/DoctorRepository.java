package com.project.back_end.repo;

import com.project.back_end.models.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    boolean existsByEmail(String email);

    Doctor findByEmailAndPassword(String email, String password);

    List<Doctor> findByNameContainingIgnoreCase(String name);

    List<Doctor> findBySpecializationIgnoreCase(String specialization);

}
