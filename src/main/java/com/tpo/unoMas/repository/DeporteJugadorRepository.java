package com.tpo.unoMas.repository;

import com.tpo.unoMas.model.DeporteJugador;
import com.tpo.unoMas.model.Jugador;
import com.tpo.unoMas.model.Deporte;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DeporteJugadorRepository extends JpaRepository<DeporteJugador, Long> {
    List<DeporteJugador> findByJugador(Jugador jugador);
    List<DeporteJugador> findByJugadorAndEsFavoritoTrue(Jugador jugador);
    DeporteJugador findByJugadorAndDeporte(Jugador jugador, Deporte deporte);
} 