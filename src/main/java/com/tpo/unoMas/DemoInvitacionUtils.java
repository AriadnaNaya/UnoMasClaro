package com.tpo.unoMas;

import com.tpo.unoMas.model.Jugador;
import com.tpo.unoMas.model.Notificacion;
import com.tpo.unoMas.model.Partido;
import com.tpo.unoMas.model.strategy.emparejamiento.EstrategiaEmparejamiento;
import com.tpo.unoMas.service.NotificacionService;
import java.util.List;
import java.util.ArrayList;

/**
 * UTILIDAD SOLO PARA DEMO
 * Métodos para simular el envío de invitaciones usando estrategias de emparejamiento.
 * No usar en producción.
 */
public class DemoInvitacionUtils {
    /**
     * Enviar invitaciones a jugadores potenciales usando una estrategia de emparejamiento (SOLO DEMO)
     */
    public static List<Jugador> enviarInvitaciones(Partido partido, List<Jugador> jugadores, EstrategiaEmparejamiento estrategia, NotificacionService notificacionService) {
        List<Jugador> potenciales = estrategia.encontrarJugadoresPotenciales(partido, jugadores);
        for (Jugador jugador : potenciales) {
            Notificacion notificacion = new Notificacion(
                "Invitación a partido",
                "Te invitamos a unirte al partido '" + partido.getTitulo() + "' el " + partido.getFechaHora(),
                jugador
            );
            // Simula el envío de notificación
            try {
                java.lang.reflect.Method m = NotificacionService.class.getDeclaredMethod("enviarConEstrategia", Notificacion.class);
                m.setAccessible(true);
                m.invoke(notificacionService, notificacion);
            } catch (Exception e) {
                System.err.println("Error enviando notificación demo: " + e.getMessage());
            }
        }
        return potenciales;
    }

    /**
     * Enviar invitaciones usando todas las estrategias (SOLO DEMO)
     */
    public static List<Jugador> enviarInvitacionesConTodasEstrategias(Partido partido, List<Jugador> jugadores, NotificacionService notificacionService) {
        EstrategiaEmparejamiento estrategiaCercania = new com.tpo.unoMas.model.strategy.emparejamiento.EmparejamientoPorCercania();
        EstrategiaEmparejamiento estrategiaNivel = new com.tpo.unoMas.model.strategy.emparejamiento.EmparejamientoPorNivel();
        EstrategiaEmparejamiento estrategiaHistorial = new com.tpo.unoMas.model.strategy.emparejamiento.EmparejamientoPorHistorial();
        List<Jugador> invitados = new ArrayList<>();
        for (EstrategiaEmparejamiento estrategia : List.of(estrategiaCercania, estrategiaNivel, estrategiaHistorial)) {
            for (Jugador jugador : estrategia.encontrarJugadoresPotenciales(partido, jugadores)) {
                if (!invitados.contains(jugador)) {
                    Notificacion notificacion = new Notificacion(
                        "Invitación a partido",
                        "Te invitamos a unirte al partido '" + partido.getTitulo() + "' el " + partido.getFechaHora(),
                        jugador
                    );
                    try {
                        java.lang.reflect.Method m = NotificacionService.class.getDeclaredMethod("enviarConEstrategia", Notificacion.class);
                        m.setAccessible(true);
                        m.invoke(notificacionService, notificacion);
                    } catch (Exception e) {
                        System.err.println("Error enviando notificación demo: " + e.getMessage());
                    }
                    invitados.add(jugador);
                }
            }
        }
        return invitados;
    }
} 