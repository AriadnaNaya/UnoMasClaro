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


@Repository
public interface JugadorRepository extends JpaRepository<Jugador, Long> {
    
      Optional<Jugador> findByEmail(String email);
    
    List<Jugador> findByZona(Zona zona);

    @Query("SELECT j FROM Jugador j WHERE j.zona.id = :zonaId")
    List<Jugador> findByZonaId(@Param("zonaId") Long zonaId);

    @Query("SELECT j FROM Jugador j WHERE j NOT IN (SELECT p.jugadores FROM Partido p WHERE p.id = :partidoId)")
    List<Jugador> findJugadoresNoEnPartido(@Param("partidoId") Long partidoId);

    boolean existsByEmail(String email);

} 