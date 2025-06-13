package com.tpo.unoMas.repository;

import com.tpo.unoMas.model.Zona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ZonaRepository extends JpaRepository<Zona, Long> {
    

    Optional<Zona> findByNombre(String nombre);
    
    boolean existsByNombre(String nombre);
} 