package com.project.back_end.services;

import com.project.back_end.models.Appointment;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final Service service; // Tu service genérico (ej: validateToken, filtros)
    private final TokenService tokenService;

    // Constructor injection
    public AppointmentService(
            AppointmentRepository appointmentRepository,
            DoctorRepository doctorRepository,
            PatientRepository patientRepository,
            Service service,
            TokenService tokenService
    ) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.service = service;
        this.tokenService = tokenService;
    }

    // Book appointment
    @Transactional
    public int bookAppointment(Appointment appointment) {
        try {
            appointmentRepository.save(appointment);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Update appointment
    @Transactional
    public int updateAppointment(Long appointmentId, Appointment updatedAppointment, Long patientId) {
        try {
            Appointment existing = appointmentRepository.findById(appointmentId).orElse(null);
            if (existing == null) return 0;
            if (!existing.getPatient().equals(patientId)) return -1; // paciente no coincide
            // Aquí puedes validar disponibilidad del doctor
            appointmentRepository.save(updatedAppointment);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Cancel appointment
    @Transactional
    public int cancelAppointment(Long appointmentId, Long patientId) {
        try {
            Appointment existing = appointmentRepository.findById(appointmentId).orElse(null);
            if (existing == null) return 0;
            if (!existing.getPatient().equals(patientId)) return -1;
            appointmentRepository.delete(existing);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Get appointments by doctor and date
    @Transactional(readOnly = true)
    public List<Appointment> getAppointments(Long doctorId, String date, String patientName) {
        // Aquí puedes implementar filtros por nombre del paciente
        return appointmentRepository.findByDoctorIdAndDate(doctorId, date);
    }

    // Change status
    @Transactional
    public boolean changeStatus(Long appointmentId, String status) {
        try {
            Appointment existing = appointmentRepository.findById(appointmentId).orElse(null);
            if (existing == null) return false;
            existing.setStatus(status);
            appointmentRepository.save(existing);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
