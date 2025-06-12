package com.tpo.unoMas.model.estado;

import com.tpo.unoMas.model.Jugador;
import com.tpo.unoMas.model.Partido;
import java.util.Objects;
public class EnJuego extends EstadoPartido{

    @Override
    public void agregarJugador(Partido partido, Jugador jugador) {
        throw new IllegalStateException("No se pueden agregar jugadores a un partido en juego");
    }

    @Override
    public void removerJugador(Partido partido, Jugador jugador) {
        throw new IllegalStateException("No se pueden remover jugadores de un partido en juego");
    }

    @Override
    public void confirmarPartido(Partido partido) {
        throw new IllegalStateException("No se puede confirmar un partido que ya está en juego");
    }

    @Override
    public void iniciarPartido(Partido partido) {
        throw new IllegalStateException("El partido ya está en juego");
    }

    @Override
    public void finalizarPartido(Partido partido) {
        Objects.requireNonNull(partido, "El partido no puede ser null");
        
        // Agregar partido al historial de todos los jugadores participantes
        for (Jugador jugador : partido.getJugadores()) {
            jugador.agregarAlHistorial(partido);
        }
        
        partido.cambiarEstado(new Finalizado());
    }

    @Override
    public void cancelarPartido(Partido partido) {
        Objects.requireNonNull(partido, "El partido no puede ser null");
        partido.cambiarEstado(new Cancelado());
    }

    @Override
    public String armarMensaje() {
        return "Ha comenzado el partido!!";
    }

}
