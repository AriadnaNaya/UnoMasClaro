package com.tpo.unoMas.model.strategy.emparejamiento;

import com.tpo.unoMas.model.Jugador;
import com.tpo.unoMas.model.Partido;
import com.tpo.unoMas.service.PartidoService;

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
                .filter(jugador -> tieneHistorialRelevante(jugador, partido))
                .limit(20) // Limitar invitaciones
                .collect(Collectors.toList());
    }
    
    @Override
    public String getDescripcion() {
        return "Busca jugadores basado en historial de partidos previos en la zona";
    }
    /**
     * Verifica si el jugador tiene historial relevante para el partido
     */
    private boolean tieneHistorialRelevante(Jugador jugador, Partido partido) {

        // Criterio 1: Ha jugado con el organizador anteriormente
        List<Partido> partidosJugador = PartidoService.obtenerPartidosPorJugador(jugador.getId());
        boolean haJugadoConOrganizador = partidosJugador.stream()
                .anyMatch(p -> p.getJugadores().contains(partido.getOrganizador()) ||
                        p.getOrganizador().equals(partido.getOrganizador()));
        
        // Criterio 2: Ha organizado partidos del mismo deporte
        boolean haOrganizadoMismoDeporte = partidosJugador.stream()
                .anyMatch(p -> p.getDeporte().equals(partido.getDeporte()));
        
        return  haJugadoConOrganizador || haOrganizadoMismoDeporte;
    }
    
    /**
     * Verifica compatibilidad básica del jugador con el partido
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

    public boolean esCompatible(Partido partido, List<Partido> historial) {
        boolean haJugadoConOrganizador = historial.stream()
            .anyMatch(p -> p.getJugadores().contains(partido.getOrganizador()) ||
                           p.getOrganizador().equals(partido.getOrganizador()));

        boolean haOrganizadoMismoDeporte = historial.stream()
            .anyMatch(p -> p.getDeporte().equals(partido.getDeporte()));

        return haJugadoConOrganizador || haOrganizadoMismoDeporte;
    }
} 