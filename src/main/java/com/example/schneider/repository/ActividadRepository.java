package com.example.schneider.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.schneider.entity.Actividad;
import com.example.schneider.entity.Usuario;

@Repository
public interface ActividadRepository extends JpaRepository<Actividad, Long> {
    
    List<Actividad> findByUsuarioOrderByFechaCreacionDesc(Usuario usuario);
    
    List<Actividad> findByUsuarioAndCompletadaOrderByFechaCreacionDesc(Usuario usuario, Boolean completada);
    
    Optional<Actividad> findByIdAndUsuario(Long id, Usuario usuario);
    
    @Query("SELECT a FROM Actividad a WHERE a.usuario = :usuario AND a.fechaVencimiento BETWEEN :inicio AND :fin")
    List<Actividad> findByUsuarioAndFechaVencimientoBetween(
        @Param("usuario") Usuario usuario, 
        @Param("inicio") LocalDateTime inicio, 
        @Param("fin") LocalDateTime fin
    );
    
    long countByUsuarioAndCompletada(Usuario usuario, Boolean completada);
}
