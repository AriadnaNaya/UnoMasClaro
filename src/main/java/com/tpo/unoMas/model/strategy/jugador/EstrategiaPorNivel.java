package com.tpo.unoMas.model.strategy.jugador;

import com.tpo.unoMas.model.DeporteJugador;
import com.tpo.unoMas.model.Jugador;
import com.tpo.unoMas.model.Nivel;
import com.tpo.unoMas.model.Partido;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Objects;

public class EstrategiaPorNivel implements EstrategiaEmparejamiento {
    @Override
    public List<Partido> encontrarPartidos(Jugador jugador, List<Partido> partidosDisponibles) {
        Objects.requireNonNull(jugador, "El jugador no puede ser null");
        return partidosDisponibles.stream()
                .filter(partido -> {
                    Nivel nivelJugador = jugador.getDeportes().stream()
                            .filter(dj -> dj.getDeporte().equals(partido.getDeporte()))
                            .map(DeporteJugador::getNivel)
                            .findFirst()
                            .orElse(null);
                    return nivelJugador != null && nivelJugador.ordinal() >= partido.getNivel().ordinal();
                })
                .toList();
    }
}