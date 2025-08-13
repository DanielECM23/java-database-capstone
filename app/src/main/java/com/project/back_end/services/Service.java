package com.project.back_end.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;
import com.project.back_end.models.Admin;
import com.project.back_end.models.Appointment;

import org.springframework.stereotype.Service;

@Service
public class ValidationService {

    private final TokenService tokenService;

    public ValidationService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    // Validar token y rol
    public boolean validateToken(String token, String role) {
        // Llama a un método válido de TokenService
        return tokenService.isValidToken(token, role);
    }

    // Validar login de administrador
    public Map<String, Object> validateAdmin(Admin admin) {
        Map<String, Object> response = new HashMap<>();

        if ("admin".equals(admin.getUsername()) && "1234".equals(admin.getPassword())) {
            response.put("status", "success");
            response.put("token", "fake-jwt-token");
        } else {
            response.put("status", "fail");
        }

        return response;
    }

    // Métodos de filtro de doctores
    public List<Doctor> filterDoctors(String name, String time, String speciality) {
        return new ArrayList<>();
    }

    public List<Doctor> filterDoctorByNameAndTime(String name, String time) {
        return new ArrayList<>();
    }

    public List<Doctor> filterDoctorByNameAndSpeciality(String name, String speciality) {
        return new ArrayList<>();
    }

    public List<Doctor> filterDoctorByTimeAndSpeciality(String time, String speciality) {
        return new ArrayList<>();
    }

    public List<Doctor> filterDoctorBySpeciality(String speciality) {
        return new ArrayList<>();
    }

    public List<Doctor> filterDoctorsByTime(String time) {
        return new ArrayList<>();
    }

    // Validaciones de citas y pacientes
    public int validateAppointment(Long doctorId, String dateTime) {
        return 1;
    }

    public boolean validatePatient(Patient patient) {
        return true;
    }

    public boolean validatePatientLogin(String email, String password) {
        return true;
    }

    public List<Appointment> filterPatient(Long patientId, String condition, String doctorName) {
        return new ArrayList<>();
    }

    public Optional<Doctor> getDoctorById(Long doctorId) {
        return Optional.empty();
    }

    public Optional<Patient> getPatientById(Long patientId) {
        return Optional.empty();
    }
}
