package com.example.schneider.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.schneider.dto.ActividadRequest;
import com.example.schneider.dto.ActividadResponse;
import com.example.schneider.service.ActividadService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/actividades")
@PreAuthorize("hasRole('USER')")
public class ActividadController {
    
    @Autowired
    private ActividadService actividadService;
    
    @GetMapping
    public ResponseEntity<List<ActividadResponse>> getAllActividades() {
        List<ActividadResponse> actividades = actividadService.getAllActividades();
        return ResponseEntity.ok(actividades);
    }
    
    @GetMapping("/completadas")
    public ResponseEntity<List<ActividadResponse>> getActividadesCompletadas() {
        List<ActividadResponse> actividades = actividadService.getActividadesByEstado(true);
        return ResponseEntity.ok(actividades);
    }
    
    @GetMapping("/pendientes")
    public ResponseEntity<List<ActividadResponse>> getActividadesPendientes() {
        List<ActividadResponse> actividades = actividadService.getActividadesByEstado(false);
        return ResponseEntity.ok(actividades);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getActividadById(@PathVariable Long id) {
        try {
            ActividadResponse actividad = actividadService.getActividadById(id);
            return ResponseEntity.ok(actividad);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<?> createActividad(@Valid @RequestBody ActividadRequest request) {
        try {
            System.out.println("hola");
            ActividadResponse actividad = actividadService.createActividad(request);
            return ResponseEntity.ok(actividad);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Error al crear la actividad: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateActividad(@PathVariable Long id, @Valid @RequestBody ActividadRequest request) {
        try {
            ActividadResponse actividad = actividadService.updateActividad(id, request);
            return ResponseEntity.ok(actividad);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<?> toggleCompletada(@PathVariable Long id) {
        try {
            ActividadResponse actividad = actividadService.toggleCompletada(id);
            return ResponseEntity.ok(actividad);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteActividad(@PathVariable Long id) {
        try {
            actividadService.deleteActividad(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Actividad eliminada exitosamente");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getEstadisticas() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("completadas", actividadService.getActividadesCompletadas());
        stats.put("pendientes", actividadService.getActividadesPendientes());
        return ResponseEntity.ok(stats);
    }
}
