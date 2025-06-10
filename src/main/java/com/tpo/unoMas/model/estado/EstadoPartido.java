package com.tpo.unoMas.model.estado;

import com.tpo.unoMas.model.Jugador;
import com.tpo.unoMas.model.Partido;

public abstract class EstadoPartido {

    public EstadoPartido() {
    }

    public abstract void agregarJugador(Partido partido, Jugador jugador);
    public abstract void removerJugador(Partido partido, Jugador jugador);
    public abstract void confirmarPartido(Partido partido);
    public abstract void iniciarPartido(Partido partido);
    public abstract void finalizarPartido(Partido partido);
    public abstract void cancelarPartido(Partido partido);

}