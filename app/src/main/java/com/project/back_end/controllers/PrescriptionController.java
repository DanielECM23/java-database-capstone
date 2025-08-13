package com.project.back_end.controllers;

import com.project.back_end.models.Prescription;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.PrescriptionService;
import com.project.back_end.services.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.path}prescription")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;
    private final Service service; // Para validar tokens, roles, etc.
    private final AppointmentService appointmentService;

    @Autowired
    public PrescriptionController(PrescriptionService prescriptionService,
                                  Service service,
                                  AppointmentService appointmentService) {
        this.prescriptionService = prescriptionService;
        this.service = service;
        this.appointmentService = appointmentService;
    }

    // Guardar nueva prescripción
    @PostMapping("/save/{token}")
    public ResponseEntity<?> savePrescription(@RequestBody Prescription prescription,
                                              @PathVariable("token") String token) {
        // Validar token y rol "doctor"
        if (!service.validateToken(token, "doctor")) {
            return ResponseEntity.status(403).body("Token inválido o sin permisos de doctor");
        }

        // Actualizar estado de la cita
        boolean updated = appointmentService.changeStatus(prescription.getAppointmentId(), "prescription_added");
        if (!updated) {
            return ResponseEntity.badRequest().body("No se pudo actualizar el estado de la cita");
        }

        // Guardar la prescripción
        ResponseEntity<?> savedResponse = prescriptionService.savePrescription(prescription);
        return savedResponse; // Ya contiene mensaje y estado
    }

    // Obtener prescripción por ID de cita
    @GetMapping("/get/{appointmentId}/{token}")
    public ResponseEntity<?> getPrescription(@PathVariable("appointmentId") Long appointmentId,
                                             @PathVariable("token") String token) {
        // Validar token y rol "doctor"
        if (!service.validateToken(token, "doctor")) {
            return ResponseEntity.status(403).body("Token inválido o sin permisos de doctor");
        }

        ResponseEntity<?> prescriptionResponse = prescriptionService.getPrescriptionByAppointmentId(appointmentId);
        return prescriptionResponse; // Ya maneja null o errores dentro del service
    }
}
