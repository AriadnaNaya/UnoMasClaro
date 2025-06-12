package com.tpo.unoMas.model.strategy.emparejamiento;

import com.tpo.unoMas.model.Jugador;
import com.tpo.unoMas.model.Partido;

import java.util.List;

public interface EstrategiaEmparejamiento {
    
    List<Jugador> encontrarJugadoresPotenciales(Partido partido, List<Jugador> jugadoresDisponibles);
    
    String getDescripcion();

    boolean esCompatible(Partido partido, Jugador jugador);
}