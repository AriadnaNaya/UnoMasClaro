package com.tpo.unoMas.model.estado;

import com.tpo.unoMas.model.Jugador;
import com.tpo.unoMas.model.Partido;

public class Confirmado extends EstadoPartido{
    @Override
    public void agregarJugador(Partido partido, Jugador jugador) {
        // STATE PATTERN: En estado confirmado, normalmente no se permiten cambios
        throw new IllegalStateException("No se pueden agregar jugadores a un partido ya confirmado");
    }

    @Override
    public void removerJugador(Partido partido, Jugador jugador) {
        // STATE PATTERN: Permitir remover pero con consecuencias
        if (!partido.getJugadores().contains(jugador)) {
            throw new IllegalStateException("El jugador no está en el partido");
        }

        partido.removerJugadorInterno(jugador);

        if (partido.getJugadores().size() < partido.getMinJugadores()) {
            partido.cambiarEstado(new NecesitamosJugadores());
        }
    }

    @Override
    public void confirmarPartido(Partido partido) {
        throw new IllegalStateException("El partido ya está confirmado");
    }

    @Override
    public void iniciarPartido(Partido partido) {
        partido.cambiarEstado(new EnJuego());
    }

    @Override
    public void finalizarPartido(Partido partido) {
        throw new IllegalStateException("No se puede finalizar un partido que no ha comenzado");
    }

    @Override
    public void cancelarPartido(Partido partido) {
        partido.cambiarEstado(new Cancelado());
    }
}
