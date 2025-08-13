package com.project.back_end.repo;

import com.project.back_end.models.Prescription;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionRepository extends MongoRepository<Prescription, String> {

    /**
     * Custom query method to find prescriptions by appointment ID.
     * Spring Data MongoDB automatically derives the query based on the method name.
     *
     * @param appointmentId the ID of the appointment
     * @return list of prescriptions associated with the appointment
     */
    List<Prescription> findByAppointmentId(Long appointmentId);
}
