package com.tpo.unoMas.repository;

import com.tpo.unoMas.model.Partido;
import com.tpo.unoMas.model.Jugador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio para la entidad Partido
 */
@Repository
public interface PartidoRepository extends JpaRepository<Partido, Long> {
    
    /**
     * Buscar partidos que contengan un jugador específico
     */
    List<Partido> findByJugadoresContaining(Jugador jugador);
    
    /**
     * Buscar partidos por organizador
     */
    List<Partido> findByOrganizador(Jugador organizador);
    
    /**
     * Buscar partidos en una zona específica
     */
    @Query("SELECT p FROM Partido p WHERE p.zona.id = :zonaId")
    List<Partido> findByZonaId(@Param("zonaId") Long zonaId);
    
    /**
     * Buscar partidos por deporte
     */
    @Query("SELECT p FROM Partido p WHERE p.deporte.id = :deporteId")
    List<Partido> findByDeporteId(@Param("deporteId") Long deporteId);
    
    /**
     * Buscar partidos en un rango de fechas
     */
    @Query("SELECT p FROM Partido p WHERE p.fechaHora BETWEEN :fechaInicio AND :fechaFin")
    List<Partido> findByFechaHoraBetween(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                       @Param("fechaFin") LocalDateTime fechaFin);
    
    /**
     * Buscar partidos que necesiten jugadores
     */
    @Query("SELECT p FROM Partido p WHERE SIZE(p.jugadores) < p.maxJugadores")
    List<Partido> findPartidosConEspacioDisponible();
    
    /**
     * Buscar partidos por estado específico
     */
    @Query("SELECT p FROM Partido p WHERE TYPE(p.estado) = :tipoEstado")
    List<Partido> findByTipoEstado(@Param("tipoEstado") Class<?> tipoEstado);
} 