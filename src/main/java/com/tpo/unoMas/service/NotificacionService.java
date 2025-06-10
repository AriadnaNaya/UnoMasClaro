package com.tpo.unoMas.service;

import com.tpo.unoMas.model.Notificacion;
import com.tpo.unoMas.model.Partido;
import com.tpo.unoMas.model.strategy.notificacion.INotificacionStrategy;
import org.springframework.stereotype.Service;

import com.tpo.unoMas.observer.Observer;

@Service
public class NotificacionService implements Observer {

    private INotificacionStrategy estrategiaNotificacion;


    public void cambiarEstrategiaNotificacion(INotificacionStrategy nuevaEstrategia) {
        this.estrategiaNotificacion = nuevaEstrategia;
    }

    @Override
    public void update(Partido partido, String estadoNuevo, String mensaje) {
        String titulo = String.format("Partido %s: Cambio a %s",
                partido.getTitulo(), estadoNuevo);

        notificarConTitulo(partido, titulo, mensaje);
    }

    public void notificarConTitulo(Partido partido, String titulo, String mensaje) {
        partido.getJugadores().forEach(jugador -> {
            Notificacion notificacion = new Notificacion(titulo, mensaje, jugador);

            enviarConEstrategia(notificacion);
        });
    }
    private void enviarConEstrategia(Notificacion notificacion) {
        try {
            estrategiaNotificacion.enviarNotificacion(notificacion);
        } catch (Exception e) {
            // Log el error pero no falla todo el proceso
            System.err.println("Error enviando notificación: " + e.getMessage());
        }
    }

    public String getEstrategiaActual() {
        return estrategiaNotificacion.getClass().getSimpleName();
    }
}