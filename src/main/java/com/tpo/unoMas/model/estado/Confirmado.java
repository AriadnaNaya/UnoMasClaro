package com.tpo.unoMas.model.estado;

import com.tpo.unoMas.model.Jugador;
import com.tpo.unoMas.model.Partido;

public class Confirmado extends EstadoPartido{
    @Override
    public void agregarJugador(Partido partido, Jugador jugador) {
        throw new IllegalStateException("No se pueden agregar jugadores a un partido ya confirmado");
    }

    @Override
    public void removerJugador(Partido partido, Jugador jugador) {
        if (!partido.getJugadores().contains(jugador)) {
            throw new IllegalStateException("El jugador no está en el partido");
        }

        partido.removerJugadorInterno(jugador);

        if (partido.getJugadores().size() < partido.getDeporte().getCantidadJugadores()) {
            partido.cambiarEstado(new NecesitamosJugadores());
            partido.setEstadoDB("Confirmado");
        }
    }

    @Override
    public void confirmarPartido(Partido partido) {
        throw new IllegalStateException("El partido ya está confirmado");
    }

    @Override
    public void iniciarPartido(Partido partido) {

        partido.cambiarEstado(new EnJuego());
        partido.setEstadoDB("EnJuego");
    }

    @Override
    public void finalizarPartido(Partido partido) {
        throw new IllegalStateException("No se puede finalizar un partido que no ha comenzado");
    }

    @Override
    public void cancelarPartido(Partido partido) {
        partido.cambiarEstado(new Cancelado());
        partido.setEstadoDB("Cancelado");
    }

    @Override
    public String armarMensaje() {
        return "El Partido ha sido confirmado";
    }
}
