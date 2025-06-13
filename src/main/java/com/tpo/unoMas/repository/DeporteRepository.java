package com.tpo.unoMas.repository;

import com.tpo.unoMas.model.Deporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface DeporteRepository extends JpaRepository<Deporte, Long> {

    Optional<Deporte> findByNombre(String nombre);
    
    boolean existsByNombre(String nombre);
} 