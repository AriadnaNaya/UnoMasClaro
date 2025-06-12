package com.tpo.unoMas.model.estado;
import com.tpo.unoMas.model.Jugador;
import com.tpo.unoMas.model.Partido;

public class Cancelado extends EstadoPartido{
    // ESTADO FINAL
    @Override
    public void agregarJugador(Partido partido, Jugador jugador) {
        throw new IllegalStateException("No se pueden agregar jugadores a un partido cancelado");
    }

    @Override
    public void removerJugador(Partido partido, Jugador jugador) {
        throw new IllegalStateException("No se pueden remover jugadores de un partido cancelado");
    }

    @Override
    public void confirmarPartido(Partido partido) {
        throw new IllegalStateException("No se puede confirmar un partido cancelado");
    }

    @Override
    public void iniciarPartido(Partido partido) {
        throw new IllegalStateException("No se puede iniciar un partido cancelado");
    }

    @Override
    public void finalizarPartido(Partido partido) {
        throw new IllegalStateException("No se puede finalizar un partido cancelado");
    }

    @Override
    public void cancelarPartido(Partido partido) {
        throw new IllegalStateException("El partido ya está cancelado");
    }

    @Override
    public String armarMensaje() {
        return "El Partido ha sido cancelado";
    }

}
