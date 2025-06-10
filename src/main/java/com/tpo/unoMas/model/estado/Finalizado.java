package com.tpo.unoMas.model.estado;

import com.tpo.unoMas.model.Jugador;
import com.tpo.unoMas.model.Partido;

public class Finalizado extends EstadoPartido{
    //ESTADO FINAL

    @Override
    public void agregarJugador(Partido partido, Jugador jugador) {
        throw new IllegalStateException("No se pueden agregar jugadores a un partido finalizado");
    }

    @Override
    public void removerJugador(Partido partido, Jugador jugador) {
        throw new IllegalStateException("No se pueden remover jugadores de un partido finalizado");
    }

    @Override
    public void confirmarPartido(Partido partido) {
        throw new IllegalStateException("No se puede confirmar un partido finalizado");
    }

    @Override
    public void iniciarPartido(Partido partido) {
        throw new IllegalStateException("No se puede iniciar un partido finalizado");
    }

    @Override
    public void finalizarPartido(Partido partido) {
        throw new IllegalStateException("El partido ya está finalizado");
    }

    @Override
    public void cancelarPartido(Partido partido) {
        throw new IllegalStateException("No se puede cancelar un partido finalizado");
    }

}
