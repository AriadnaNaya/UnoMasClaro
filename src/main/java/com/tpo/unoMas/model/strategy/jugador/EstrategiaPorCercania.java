package com.tpo.unoMas.model.strategy.jugador;

import com.tpo.unoMas.model.Jugador;
import com.tpo.unoMas.model.Partido;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EstrategiaPorCercania implements EstrategiaEmparejamiento {

    @Override
    public List<Partido> encontrarPartidos(Jugador jugador, List<Partido> partidosDisponibles) {
        if (jugador.getZona() == null) {
            return partidosDisponibles;
        }

        return partidosDisponibles.stream()
                .filter(partido -> partido.getZona() != null &&
                        partido.getZona().getPartido().equals(jugador.getZona().getPartido()))
                .collect(Collectors.toList());
    }
}