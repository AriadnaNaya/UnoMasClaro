package com.tpo.unoMas.model.strategy.emparejamiento;

import com.tpo.unoMas.model.Jugador;
import com.tpo.unoMas.model.Partido;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Estrategia de emparejamiento por cercanía geográfica
 * Busca jugadores en la misma zona que el partido
 */
public class EmparejamientoPorCercania implements EstrategiaEmparejamiento {

    @Override
    public List<Jugador> encontrarJugadoresPotenciales(Partido partido, List<Jugador> jugadoresDisponibles) {
        if (partido.getZona() == null) {
            return jugadoresDisponibles; // Si no hay zona definida, incluir a todos
        }
        
        return jugadoresDisponibles.stream()
                .filter(jugador -> jugador.getZona() != null && 
                               jugador.getZona().getPartido().equals(partido.getZona().getPartido()))
                .filter(jugador -> esJugadorCompatible(jugador, partido))
                .limit(20) // Limitar a 20 invitaciones para no saturar
                .collect(Collectors.toList());
    }
    
    @Override
    public String getDescripcion() {
        return "Busca jugadores en la misma zona geográfica del partido";
    }
    
    /**
     * Verifica si el jugador es compatible con el partido (deporte, nivel, etc.)
     */
    private boolean esJugadorCompatible(Jugador jugador, Partido partido) {
        // Verificar que el jugador practique el deporte del partido
        boolean practicaDeporte = jugador.getDeportes().stream()
                .anyMatch(dj -> dj.getDeporte().equals(partido.getDeporte()));
                
        // Verificar que no sea el organizador
        boolean noEsOrganizador = !jugador.equals(partido.getOrganizador());
        
        // Verificar que no esté ya en el partido
        boolean noEstaEnPartido = !partido.getJugadores().contains(jugador);
        
        return practicaDeporte && noEsOrganizador && noEstaEnPartido;
    }
} 