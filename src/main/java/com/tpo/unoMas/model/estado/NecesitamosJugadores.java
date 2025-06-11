package com.tpo.unoMas.model.estado;

import com.tpo.unoMas.model.Jugador;
import com.tpo.unoMas.model.Partido;

public class NecesitamosJugadores extends EstadoPartido {
    @Override
    public void agregarJugador(Partido partido, Jugador jugador) {

        if (partido.getJugadores().size() > partido.getDeporte().getCantidadJugadores()) {
            throw new IllegalStateException("El partido ya tiene el máximo de jugadores permitidos");
        }

        partido.agregarJugadorInterno(jugador);

        if (partido.getJugadores().size() == partido.getDeporte().getCantidadJugadores()) {
            partido.cambiarEstado(new PartidoArmado());
        }
    }

    @Override
    public void removerJugador(Partido partido, Jugador jugador) {
        partido.removerJugadorInterno(jugador);
    }

    @Override
    public void confirmarPartido(Partido partido) {
        throw new IllegalStateException("No se puede confirmar un partido que aún necesita jugadores");
    }

    @Override
    public void iniciarPartido(Partido partido) {
        throw new IllegalStateException("No se puede confirmar un partido que aún necesita jugadores");
    }

    @Override
    public void finalizarPartido(Partido partido) {
        throw new IllegalStateException("No se puede confirmar un partido que aún necesita jugadores");
    }

    @Override
    public void cancelarPartido(Partido partido) {
        throw new IllegalStateException("No se puede confirmar un partido que aún necesita jugadores");
    }

    @Override
    public String armarMensaje() {
        return "El Partido ha sido creado";
    }
}
