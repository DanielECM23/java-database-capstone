package com.project.back_end.services;

import com.project.back_end.models.Doctor;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;

    @Autowired
    public DoctorService(DoctorRepository doctorRepository,
                         AppointmentRepository appointmentRepository) {
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
    }

    public boolean existsByEmail(String email) {
        return doctorRepository.existsByEmail(email);
    }

    public boolean existsById(Long id) {
        return doctorRepository.existsById(id);
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<String> getDoctorAvailability(Long doctorId, String dateString) {
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        if (doctorOpt.isEmpty()) return List.of();

        Doctor doctor = doctorOpt.get();

        // Convertir String a LocalDate
        LocalDate date = LocalDate.parse(dateString);

        // Filtrar slots disponibles
        return doctor.getAvailableTimes().stream()
                .filter(slot -> appointmentRepository
                        .findByDoctorIdAndAppointmentTimeBetween(
                                doctorId,
                                date.atStartOfDay(),
                                date.plusDays(1).atStartOfDay()
                        ).stream()
                        .noneMatch(a -> a.getAppointmentTime().equals(slot)))
                .collect(Collectors.toList());
    }

    @Transactional
    public int saveDoctor(Doctor doctor) {
        if (doctorRepository.existsByEmail(doctor.getEmail())) return -1;
        try {
            doctorRepository.save(doctor);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Transactional
    public int updateDoctor(Doctor doctor) {
        if (!doctorRepository.existsById(doctor.getId())) return -1;
        try {
            doctorRepository.save(doctor);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Transactional
    public int deleteDoctor(Long doctorId) {
        if (!doctorRepository.existsById(doctorId)) return -1;
        try {
            appointmentRepository.deleteAllByDoctorId(doctorId);
            doctorRepository.deleteById(doctorId);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Transactional(readOnly = true)
    public Doctor validateDoctor(String email, String password) {
        return doctorRepository.findByEmailAndPassword(email, password).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Doctor> findDoctorByName(String name) {
        return doctorRepository.findByNameContainingIgnoreCase(name);
    }

    @Transactional(readOnly = true)
    public List<Doctor> filterDoctorBySpeciality(String speciality) {
        return doctorRepository.findBySpecializationIgnoreCase(speciality);
    }

    @Transactional(readOnly = true)
    public boolean checkAvailability(Long doctorId, String dateString) {
        return !getDoctorAvailability(doctorId, dateString).isEmpty();
    }
}
