package com.tpo.unoMas.repository;

import com.tpo.unoMas.model.Deporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para la entidad Deporte
 */
@Repository
public interface DeporteRepository extends JpaRepository<Deporte, Long> {
    
    /**
     * Buscar deporte por nombre
     */
    Optional<Deporte> findByNombre(String nombre);
    
    /**
     * Verificar si existe deporte con nombre
     */
    boolean existsByNombre(String nombre);
} 