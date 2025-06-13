package com.tpo.unoMas.repository;

import com.tpo.unoMas.model.Partido;
import com.tpo.unoMas.model.Jugador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PartidoRepository extends JpaRepository<Partido, Long> {

    @Query("SELECT p FROM Partido p JOIN p.jugadores j WHERE j = :jugador")
    List<Partido> findByJugadoresContaining(@Param("jugador") Jugador jugador);

    @Query("SELECT p FROM Partido p WHERE p.organizador = :organizador")
    List<Partido> findByOrganizador(@Param("organizador") Jugador organizador);

    @Query("SELECT p FROM Partido p WHERE p.zona.id = :zonaId")
    List<Partido> findByZonaId(@Param("zonaId") Long zonaId);

    @Query("SELECT p FROM Partido p WHERE p.deporte.id = :deporteId")
    List<Partido> findByDeporteId(@Param("deporteId") Long deporteId);

    @Query("SELECT p FROM Partido p WHERE p.fechaHora BETWEEN :fechaInicio AND :fechaFin")
    List<Partido> findByFechaHoraBetween(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                       @Param("fechaFin") LocalDateTime fechaFin);
    
    @Query("SELECT p FROM Partido p WHERE SIZE(p.jugadores) < p.deporte.cantidadJugadores")
    List<Partido> findPartidosConEspacioDisponible();

    @Query("SELECT p FROM Partido p WHERE p.fechaHora > CURRENT_TIMESTAMP " +
           "AND SIZE(p.jugadores) < p.deporte.cantidadJugadores " +
           "AND p.estadoDB = 'NecesitamosJugadores'")
    List<Partido> findPartidosActivos();
} 