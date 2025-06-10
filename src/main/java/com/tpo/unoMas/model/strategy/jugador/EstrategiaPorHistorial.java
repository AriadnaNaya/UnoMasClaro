package com.tpo.unoMas.model.strategy.jugador;

import com.tpo.unoMas.model.Jugador;
import com.tpo.unoMas.model.Partido;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EstrategiaPorHistorial implements EstrategiaEmparejamiento {

    @Override
    public List<Partido> encontrarPartidos(Jugador jugador, List<Partido> partidosDisponibles) {
        return partidosDisponibles.stream()
                .filter(partido -> {
                    // Verificar si el jugador ha jugado en la misma zona
                    boolean haJugadoEnZona = jugador.getPartidosParticipados().stream()
                            .anyMatch(p -> p.getZona().equals(partido.getZona()));

                    return haJugadoEnZona;
                })
                .collect(Collectors.toList());
    }
}