package com.tpo.unoMas.service;

import com.tpo.unoMas.model.Jugador;
import com.tpo.unoMas.model.Notificacion;
import com.tpo.unoMas.model.Partido;
import com.tpo.unoMas.model.observer.Observer;
import com.tpo.unoMas.model.strategy.notificacion.INotificacionStrategy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificacionService implements Observer {

    private INotificacionStrategy estrategiaNotificacion;


    public void cambiarEstrategiaNotificacion(INotificacionStrategy nuevaEstrategia) {
        this.estrategiaNotificacion = nuevaEstrategia;
    }

    //Avisa sobre cambio de estado
    @Override
    public void update(Partido partido) {
        String titulo = String.format("Partido %s: Cambio a %s",
                partido.getTitulo(), partido.getEstado().getClass().getSimpleName());

        notificarConTitulo(partido.getJugadores(), titulo, partido.getEstado().armarMensaje());
    }

    //Le envia a cada jugador una notificacion segun la estrategia definida
    public void notificarConTitulo(List<Jugador> jugadores, String titulo, String mensaje) {
        jugadores.forEach(jugador -> {
            Notificacion notificacion = new Notificacion(titulo, mensaje, jugador);
            enviarConEstrategia(notificacion);
        });
    }
    private void enviarConEstrategia(Notificacion notificacion) {
        try {
            estrategiaNotificacion.enviarNotificacion(notificacion);
        } catch (Exception e) {
            System.err.println("Error enviando notificación: " + e.getMessage());
        }
    }

}