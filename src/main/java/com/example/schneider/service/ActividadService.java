package com.example.schneider.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.schneider.dto.ActividadRequest;
import com.example.schneider.dto.ActividadResponse;
import com.example.schneider.entity.Actividad;
import com.example.schneider.entity.Usuario;
import com.example.schneider.repository.ActividadRepository;
import com.example.schneider.repository.UsuarioRepository;
import com.example.schneider.security.UserPrincipal;

@Service
public class ActividadService {
    
    @Autowired
    private ActividadRepository actividadRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    private Usuario getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return usuarioRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
    
    private ActividadResponse convertToResponse(Actividad actividad) {
        return new ActividadResponse(
            actividad.getId(),
            actividad.getTitulo(),
            actividad.getDescripcion(),
            actividad.getCompletada(),
            actividad.getFechaCreacion(),
            actividad.getFechaActualizacion(),
            actividad.getFechaVencimiento(),
            actividad.getUsuario().getId(),
            actividad.getUsuario().getEmail()
        );
    }
    
    public List<ActividadResponse> getAllActividades() {
        Usuario usuario = getCurrentUser();
        List<Actividad> actividades = actividadRepository.findByUsuarioOrderByFechaCreacionDesc(usuario);
        return actividades.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    public List<ActividadResponse> getActividadesByEstado(Boolean completada) {
        Usuario usuario = getCurrentUser();
        List<Actividad> actividades = actividadRepository.findByUsuarioAndCompletadaOrderByFechaCreacionDesc(usuario, completada);
        return actividades.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    public ActividadResponse getActividadById(Long id) {
        Usuario usuario = getCurrentUser();
        Actividad actividad = actividadRepository.findByIdAndUsuario(id, usuario)
                .orElseThrow(() -> new RuntimeException("Actividad no encontrada"));
        return convertToResponse(actividad);
    }
    
    public ActividadResponse createActividad(ActividadRequest request) {
        Usuario usuario = getCurrentUser();
        
        Actividad actividad = new Actividad();
        actividad.setTitulo(request.getTitulo());
        actividad.setDescripcion(request.getDescripcion());
        actividad.setFechaVencimiento(request.getFechaVencimiento());
        actividad.setUsuario(usuario);
        
        Actividad savedActividad = actividadRepository.save(actividad);
        return convertToResponse(savedActividad);
    }
    
    public ActividadResponse updateActividad(Long id, ActividadRequest request) {
        Usuario usuario = getCurrentUser();
        Actividad actividad = actividadRepository.findByIdAndUsuario(id, usuario)
                .orElseThrow(() -> new RuntimeException("Actividad no encontrada"));
        
        actividad.setTitulo(request.getTitulo());
        actividad.setDescripcion(request.getDescripcion());
        actividad.setFechaVencimiento(request.getFechaVencimiento());
        
        Actividad updatedActividad = actividadRepository.save(actividad);
        return convertToResponse(updatedActividad);
    }
    
    public ActividadResponse toggleCompletada(Long id) {
        Usuario usuario = getCurrentUser();
        Actividad actividad = actividadRepository.findByIdAndUsuario(id, usuario)
                .orElseThrow(() -> new RuntimeException("Actividad no encontrada"));
        
        actividad.setCompletada(!actividad.getCompletada());
        
        Actividad updatedActividad = actividadRepository.save(actividad);
        return convertToResponse(updatedActividad);
    }
    
    public void deleteActividad(Long id) {
        Usuario usuario = getCurrentUser();
        Actividad actividad = actividadRepository.findByIdAndUsuario(id, usuario)
                .orElseThrow(() -> new RuntimeException("Actividad no encontrada"));
        
        actividadRepository.delete(actividad);
    }
    
    public long getActividadesCompletadas() {
        Usuario usuario = getCurrentUser();
        return actividadRepository.countByUsuarioAndCompletada(usuario, true);
    }
    
    public long getActividadesPendientes() {
        Usuario usuario = getCurrentUser();
        return actividadRepository.countByUsuarioAndCompletada(usuario, false);
    }
}
