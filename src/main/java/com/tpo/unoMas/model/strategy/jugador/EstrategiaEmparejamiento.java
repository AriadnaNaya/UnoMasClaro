package com.tpo.unoMas.model.strategy.jugador;

import com.tpo.unoMas.model.Jugador;
import com.tpo.unoMas.model.Partido;

import java.util.List;

public interface EstrategiaEmparejamiento {
    List<Partido> encontrarPartidos(Jugador jugador, List<Partido> partidosDisponibles);
}