package com.tpo.unoMas.repository;

import com.tpo.unoMas.model.Zona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para la entidad Zona
 */
@Repository
public interface ZonaRepository extends JpaRepository<Zona, Long> {
    
    /**
     * Buscar zona por nombre
     */
    Optional<Zona> findByNombre(String nombre);
    
    /**
     * Verificar si existe zona con nombre
     */
    boolean existsByNombre(String nombre);
} 