package com.tpo.unoMas.model.strategy.emparejamiento;

import com.tpo.unoMas.model.DeporteJugador;
import com.tpo.unoMas.model.Jugador;
import com.tpo.unoMas.model.Nivel;
import com.tpo.unoMas.model.Partido;

import java.util.List;
import java.util.stream.Collectors;


public class EmparejamientoPorNivel implements EstrategiaEmparejamiento {

    @Override
    public List<Jugador> encontrarJugadoresPotenciales(Partido partido, List<Jugador> jugadoresDisponibles) {
        return jugadoresDisponibles.stream()
                .filter(jugador -> esJugadorCompatible(jugador, partido))
                .filter(jugador -> tieneNivelCompatible(jugador, partido))
                .limit(20) // Limitar invitaciones
                .collect(Collectors.toList());
    }
    
    @Override
    public String getDescripcion() {
        return "Busca jugadores con nivel de habilidad compatible al del partido";
    }

    @Override
    public boolean esCompatible(Partido partido, Jugador jugador) {
        return esJugadorCompatible(jugador, partido) && tieneNivelCompatible(jugador, partido);
    }

    private boolean tieneNivelCompatible(Jugador jugador, Partido partido) {
        Nivel nivelPartido = partido.getNivel();

        if (nivelPartido == null) {
            return true; // Si el partido no tiene nivel definido, acepta cualquier nivel
        }
        
        // Buscar el nivel del jugador en el deporte del partido
        Nivel nivelJugador = jugador.getDeportes().stream()
                .filter(dj -> dj.getDeporte().equals(partido.getDeporte()))
                .map(DeporteJugador::getNivel)
                .findFirst()
                .orElse(null);
                
        if (nivelJugador == null) {
            return false; // No practica el deporte
        }
        
        // Solo permite jugadores con nivel igual o superior al del partido
        return nivelJugador.ordinal() >= nivelPartido.ordinal();
    }


    private boolean esJugadorCompatible(Jugador jugador, Partido partido) {

        // Verificar que no sea el organizador
        boolean noEsOrganizador = !jugador.equals(partido.getOrganizador());

        // Verificar que no esté ya en el partido
        boolean noEstaEnPartido = !partido.getJugadores().contains(jugador);

        return noEsOrganizador && noEstaEnPartido;
    }

} 