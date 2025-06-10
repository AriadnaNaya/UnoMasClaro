package com.tpo.unoMas.repository;

import com.tpo.unoMas.model.Jugador;
import com.tpo.unoMas.model.Zona;
import com.tpo.unoMas.model.Deporte;
import com.tpo.unoMas.model.Nivel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Jugador
 */
@Repository
public interface JugadorRepository extends JpaRepository<Jugador, Long> {
    
    /**
     * Buscar jugador por email
     */
    Optional<Jugador> findByEmail(String email);
    
    /**
     * Buscar jugadores por zona
     */
    List<Jugador> findByZona(Zona zona);
    
    /**
     * Buscar jugadores por deporte favorito
     */
    List<Jugador> findByDeporteFavorito(Deporte deporte);
    
    /**
     * Buscar jugadores por nivel
     */
    List<Jugador> findByNivel(Nivel nivel);
    
    /**
     * Buscar jugadores en una zona específica por ID
     */
    @Query("SELECT j FROM Jugador j WHERE j.zona.id = :zonaId")
    List<Jugador> findByZonaId(@Param("zonaId") Long zonaId);
    
    /**
     * Buscar jugadores por rango de niveles
     */
    @Query("SELECT j FROM Jugador j WHERE j.nivel >= :nivelMinimo AND j.nivel <= :nivelMaximo")
    List<Jugador> findByNivelBetween(@Param("nivelMinimo") Nivel nivelMinimo, 
                                   @Param("nivelMaximo") Nivel nivelMaximo);
    
    /**
     * Buscar jugadores que no estén en un partido específico
     */
    @Query("SELECT j FROM Jugador j WHERE j NOT IN (SELECT p.jugadores FROM Partido p WHERE p.id = :partidoId)")
    List<Jugador> findJugadoresNoEnPartido(@Param("partidoId") Long partidoId);
    
    /**
     * Verificar si existe jugador con email
     */
    boolean existsByEmail(String email);
    
    /**
     * Buscar jugadores activos (que han jugado recientemente)
     */
    @Query("SELECT DISTINCT j FROM Jugador j JOIN j.partidosParticipados p WHERE p.fechaHora >= :fechaLimite")
    List<Jugador> findJugadoresActivos(@Param("fechaLimite") java.time.LocalDateTime fechaLimite);
} 