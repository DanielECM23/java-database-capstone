package com.project.back_end.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Document(collection = "prescriptions")
public class Prescription {

    @Id
    private String id; // ID de MongoDB

    @NotNull(message = "El nombre del paciente es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre del paciente debe tener entre 3 y 100 caracteres")
    private String patientName;

    @NotNull(message = "El ID de la cita es obligatorio")
    private Long appointmentId;

    @NotNull(message = "El nombre del medicamento es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre del medicamento debe tener entre 3 y 100 caracteres")
    private String medication;

    @NotNull(message = "La dosificación es obligatoria")
    @Size(min = 3, max = 20, message = "La dosificación debe tener entre 3 y 20 caracteres")
    private String dosage;

    @Size(max = 200, message = "Las notas del médico no pueden superar los 200 caracteres")
    private String doctorNotes;

    // Constructor vacío (necesario para Spring Data)
    public Prescription() {
    }

    // Constructor para inicializar campos principales
    public Prescription(String patientName, Long appointmentId, String medication, String dosage) {
        this.patientName = patientName;
        this.appointmentId = appointmentId;
        this.medication = medication;
        this.dosage = dosage;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getMedication() {
        return medication;
    }

    public void setMedication(String medication) {
        this.medication = medication;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getDoctorNotes() {
        return doctorNotes;
    }

    public void setDoctorNotes(String doctorNotes) {
        this.doctorNotes = doctorNotes;
    }
}
