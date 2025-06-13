package com.tpo.unoMas.model.strategy.emparejamiento;

import com.tpo.unoMas.model.Jugador;
import com.tpo.unoMas.model.Partido;

import java.util.List;
import java.util.stream.Collectors;

public class EmparejamientoPorCercania implements EstrategiaEmparejamiento {

    @Override
    public List<Jugador> encontrarJugadoresPotenciales(Partido partido, List<Jugador> jugadoresDisponibles) {
        if (partido.getZona() == null) {
            return jugadoresDisponibles;
        }
        
        return jugadoresDisponibles.stream()
                .filter(jugador -> jugador.getZona() != null && 
                               jugador.getZona().getPartido().equals(partido.getZona().getPartido()))
                .filter(jugador -> esJugadorCompatible(jugador, partido))
                .limit(20)
                .collect(Collectors.toList());
    }
    
    @Override
    public String getDescripcion() {
        return "Busca jugadores en la misma zona geográfica del partido";
    }

    private boolean esJugadorCompatible(Jugador jugador, Partido partido) {

        // Verificar que no sea el organizador
        boolean noEsOrganizador = !jugador.equals(partido.getOrganizador());

        // Verificar que no esté ya en el partido
        boolean noEstaEnPartido = !partido.getJugadores().contains(jugador);

        return  noEsOrganizador && noEstaEnPartido;
    }

    public boolean esCompatible(Partido partido, Jugador jugador) {
        if (partido.getZona() == null) {
            return esJugadorCompatible(jugador, partido);
        }
        return jugador.getZona() != null &&
               jugador.getZona().getPartido().equals(partido.getZona().getPartido()) &&
               esJugadorCompatible(jugador, partido);
    }
} 