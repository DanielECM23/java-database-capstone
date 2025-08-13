package com.project.back_end.controllers;

import com.project.back_end.models.Doctor;
import com.project.back_end.services.DoctorService;
import com.project.back_end.services.Service;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${api.path}doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private Service service;

    /**
     * GET - Check doctor availability
     */
    @GetMapping("/availability/{user}/{doctorId}/{date}/{token}")
    public ResponseEntity<Map<String, Object>> getDoctorAvailability(
            @PathVariable String user,
            @PathVariable Long doctorId,
            @PathVariable String date,
            @PathVariable String token) {

        Map<String, Object> response = new HashMap<>();

        if (!service.validateToken(user, token)) {
            response.put("error", "Invalid token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        boolean available = doctorService.checkAvailability(doctorId, date);
        response.put("available", available);
        return ResponseEntity.ok(response);
    }

    /**
     * GET - Get all doctors
     */
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getDoctor() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        Map<String, Object> response = new HashMap<>();
        response.put("doctors", doctors);
        return ResponseEntity.ok(response);
    }

    /**
     * POST - Save doctor (Admin only)
     */
    @PostMapping("/save/{token}")
    public ResponseEntity<Map<String, Object>> saveDoctor(
            @Valid @RequestBody Doctor doctor,
            @PathVariable String token) {

        Map<String, Object> response = new HashMap<>();

        if (!service.validateToken("admin", token)) {
            response.put("error", "Unauthorized");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        if (doctorService.existsByEmail(doctor.getEmail())) {
            response.put("error", "Doctor already exists");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        doctorService.saveDoctor(doctor);
        response.put("message", "Doctor registered successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }



    /**
     * PUT - Update doctor (Admin only)
     */
    @PutMapping("/update/{token}")
    public ResponseEntity<Map<String, Object>> updateDoctor(
            @Valid @RequestBody Doctor doctor,
            @PathVariable String token) {

        Map<String, Object> response = new HashMap<>();

        if (!service.validateToken("admin", token)) {
            response.put("error", "Unauthorized");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        if (!doctorService.existsById(doctor.getId())) {
            response.put("error", "Doctor not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        doctorService.updateDoctor(doctor);
        response.put("message", "Doctor updated successfully");
        return ResponseEntity.ok(response);
    }

    /**
     * DELETE - Delete doctor by ID (Admin only)
     */
    @DeleteMapping("/delete/{doctorId}/{token}")
    public ResponseEntity<Map<String, Object>> deleteDoctor(
            @PathVariable Long doctorId,
            @PathVariable String token) {

        Map<String, Object> response = new HashMap<>();

        if (!service.validateToken("admin", token)) {
            response.put("error", "Unauthorized");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        if (!doctorService.existsById(doctorId)) {
            response.put("error", "Doctor not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        doctorService.deleteDoctor(doctorId);
        response.put("message", "Doctor deleted successfully");
        return ResponseEntity.ok(response);
    }

    /**
     * GET - Filter doctors by name, time and speciality
     */
    @GetMapping("/filter/{name}/{time}/{speciality}")
    public ResponseEntity<Map<String, Object>> filter(
            @PathVariable String name,
            @PathVariable String time,
            @PathVariable String speciality) {

        List<Doctor> doctors = service.filterDoctors(name, time, speciality);
        Map<String, Object> response = new HashMap<>();
        response.put("doctors", doctors);
        return ResponseEntity.ok(response);
    }
}
