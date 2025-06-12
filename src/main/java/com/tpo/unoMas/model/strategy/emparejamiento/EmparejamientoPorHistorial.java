package com.tpo.unoMas.model.strategy.emparejamiento;

import com.tpo.unoMas.model.Jugador;
import com.tpo.unoMas.model.Partido;


import java.util.List;
import java.util.stream.Collectors;

/**
 * Estrategia de emparejamiento por historial de partidos previos
 * Busca jugadores que han jugado en la misma zona o con el organizador anteriormente
 */
public class EmparejamientoPorHistorial implements EstrategiaEmparejamiento {

    @Override
    public List<Jugador> encontrarJugadoresPotenciales(Partido partido, List<Jugador> jugadoresDisponibles) {
        return jugadoresDisponibles.stream()
                .filter(jugador -> esJugadorCompatible(jugador, partido))
                .filter(jugador -> esCompatible(partido, jugador))
                .limit(20) // Limitar invitaciones
                .collect(Collectors.toList());
    }
    @Override
    public String getDescripcion() {
        return "Busca jugadores basado en historial de partidos previos en la zona";
    }

    
    /**
     * Verifica compatibilidad básica del jugador con el partido
     */
    private boolean esJugadorCompatible(Jugador jugador, Partido partido) {
        boolean noEsOrganizador = !jugador.equals(partido.getOrganizador());
        boolean noEstaEnPartido = !partido.getJugadores().contains(jugador);
        
        return noEsOrganizador && noEstaEnPartido;
    }

    @Override
    public boolean esCompatible(Partido partido, Jugador jugador) {
        List<Partido> historial = jugador.getHistorial();

        boolean practicaDeporte = jugador.getDeportes().stream()
                .anyMatch(dj -> dj.getDeporte().equals(partido.getDeporte()));

        boolean haJugadoConOrganizador = historial.stream()
            .anyMatch(p -> p.getJugadores().contains(partido.getOrganizador()) ||
                           p.getOrganizador().equals(partido.getOrganizador()));

        boolean haOrganizadoMismoDeporte = historial.stream()
            .anyMatch(p -> p.getDeporte().equals(partido.getDeporte()));

        return practicaDeporte ||  haJugadoConOrganizador || haOrganizadoMismoDeporte;
    }
} 