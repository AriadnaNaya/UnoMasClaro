package com.tpo.unoMas.model.strategy.emparejamiento;

import com.tpo.unoMas.model.Jugador;
import com.tpo.unoMas.model.Partido;

import java.util.List;

/**
 * Strategy Pattern para algoritmos de emparejamiento de jugadores
 * Encuentra jugadores potenciales para invitar cuando se crea un partido
 */
public interface EstrategiaEmparejamiento {
    
    /**
     * Encuentra jugadores potenciales para invitar a un partido
     * @param partido El partido que necesita jugadores
     * @param jugadoresDisponibles Lista de todos los jugadores registrados
     * @return Lista de jugadores a los que se les enviará invitación
     */
    List<Jugador> encontrarJugadoresPotenciales(Partido partido, List<Jugador> jugadoresDisponibles);
    
    /**
     * Obtiene la descripción de la estrategia
     * @return Descripción del algoritmo de emparejamiento
     */
    String getDescripcion();

    boolean esCompatible(Partido partido, Jugador jugador);
} 