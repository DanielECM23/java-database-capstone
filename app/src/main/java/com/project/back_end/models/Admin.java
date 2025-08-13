package com.project.back_end.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "admins")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "username no puede ser nulo")
    private String username;

    @NotNull(message = "password no puede ser nulo")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // Se puede enviar pero no se mostrará en las respuestas
    private String password;

    // Constructor vacío (obligatorio para JPA)
    public Admin() {
    }

    // Constructor con parámetros
    public Admin(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

