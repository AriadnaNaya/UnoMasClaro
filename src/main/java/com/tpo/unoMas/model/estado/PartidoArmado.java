package com.tpo.unoMas.model.estado;

import com.tpo.unoMas.model.Jugador;
import com.tpo.unoMas.model.Partido;
import java.util.Objects;

public class PartidoArmado extends EstadoPartido {


    @Override
    public void agregarJugador(Partido partido, Jugador jugador) {
        Objects.requireNonNull(jugador, "El jugador no puede ser null");

        if (partido.getJugadores().contains(jugador)) {
            throw new IllegalStateException("El jugador ya está en el partido");
        }

        if (partido.estaCompleto()) {
            throw new IllegalStateException("El partido ya tiene todos los jugadores necesarios");
        }

        partido.agregarJugadorInterno(jugador);
    }

    @Override
    public void removerJugador(Partido partido, Jugador jugador) {
        Objects.requireNonNull(jugador, "El jugador no puede ser null");

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
        partido.cambiarEstado(new Confirmado());
    }

    @Override
    public void iniciarPartido(Partido partido) {
        throw new IllegalStateException("No se puede iniciar un partido que no está confirmado");
    }

    @Override
    public void finalizarPartido(Partido partido) {
        throw new IllegalStateException("No se puede finalizar un partido que no está en juego");
    }

    @Override
    public void cancelarPartido(Partido partido) {
        partido.cambiarEstado(new Cancelado());
    }

    @Override
    public String armarMensaje() {
        return "Tu Partido ya esta listo, hora de confirmar asistencia!!";
    }
}
