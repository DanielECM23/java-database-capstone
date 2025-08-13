package com.project.back_end.controllers;

import com.project.back_end.models.Admin;
import com.project.back_end.services.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("${api.path}admin")  // Base path configurable desde application.properties
public class AdminController {

    private final Service service;

    // Constructor para inyección de dependencias
    @Autowired
    public AdminController(Service service) {
        this.service = service;
    }

    // Endpoint de login de administrador
    @PostMapping("/login")

    public ResponseEntity<Map<String, Object>> adminLogin(@RequestBody Admin admin) {
        String credentials = admin.getUsername() + ":" + admin.getPassword();
        Map<String, Object> response = service.validateAdmin(credentials);
        return ResponseEntity.ok(response);
    }

}
