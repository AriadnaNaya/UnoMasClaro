package com.tpo.unoMas.demo;

import com.tpo.unoMas.model.*;
import com.tpo.unoMas.model.strategy.emparejamiento.EstrategiaEmparejamiento;
import com.tpo.unoMas.service.NotificacionService;
import com.tpo.unoMas.repository.ZonaRepository;
import com.tpo.unoMas.repository.DeporteRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * UTILIDADES SOLO PARA DEMO Y TESTING
 * Métodos para simular funcionalidades del sistema y crear datos de prueba.
 * NO USAR EN PRODUCCIÓN.
 */
public class DemoUtils {
    
    /**
     * Crear datos de prueba en la base de datos (SOLO DEMO)
     * Equivalente al endpoint /api/configuracion/datos-prueba
     */
    public static Map<String, Object> crearDatosPrueba(ZonaRepository zonaRepository, DeporteRepository deporteRepository) {
        try {
            // Crear zonas de prueba si no existen
            if (zonaRepository.count() == 0) {
                // Crear ubicaciones
                Ubicacion ubicacionPalermo = new Ubicacion();
                ubicacionPalermo.setLatitud(-34.5875);
                ubicacionPalermo.setLongitud(-58.4217);
                
                Ubicacion ubicacionBelgrano = new Ubicacion();
                ubicacionBelgrano.setLatitud(-34.5631);
                ubicacionBelgrano.setLongitud(-58.4550);
                
                Ubicacion ubicacionSanTelmo = new Ubicacion();
                ubicacionSanTelmo.setLatitud(-34.6219);
                ubicacionSanTelmo.setLongitud(-58.3731);

                // Crear zonas usando el constructor correcto
                Zona zona1 = new Zona("Palermo", "CABA", ubicacionPalermo);
                zonaRepository.save(zona1);

                Zona zona2 = new Zona("Belgrano", "CABA", ubicacionBelgrano);
                zonaRepository.save(zona2);

                Zona zona3 = new Zona("San Telmo", "CABA", ubicacionSanTelmo);
                zonaRepository.save(zona3);
            }

            // Crear deportes de prueba si no existen
            if (deporteRepository.count() == 0) {
                // Usar el constructor correcto de Deporte
                Deporte futbol = new Deporte("Fútbol 11", "Deporte de equipo jugado con los pies", 22);
                deporteRepository.save(futbol);

                Deporte tenis = new Deporte("Tenis", "Deporte de raqueta individual o por parejas", 2);
                deporteRepository.save(tenis);

                Deporte basquet = new Deporte("Básquet", "Deporte de equipo jugado con las manos", 10);
                deporteRepository.save(basquet);
            }

            return Map.of(
                "mensaje", "Datos de prueba creados exitosamente",
                "zonas", zonaRepository.count(),
                "deportes", deporteRepository.count()
            );
        } catch (Exception e) {
            return Map.of(
                "error", "Error al crear datos de prueba",
                "detalle", e.getMessage()
            );
        }
    }

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

    /**
     * Crear un jugador de prueba (SOLO DEMO)
     */
    public static Jugador crearJugadorDemo(String nombre, String email, Zona zona, Deporte deporte, Nivel nivel) {
        Jugador jugador = new Jugador(nombre, email, "password123", zona);
        
        // Agregar deporte con nivel
        DeporteJugador deporteJugador = new DeporteJugador();
        deporteJugador.setJugador(jugador);
        deporteJugador.setDeporte(deporte);
        deporteJugador.setNivel(nivel);
        jugador.getDeportes().add(deporteJugador);
        
        return jugador;
    }

    /**
     * Crear un partido de prueba (SOLO DEMO)
     */
    public static Partido crearPartidoDemo(String titulo, Zona zona, Deporte deporte, Nivel nivel, Jugador organizador, int diasDesdeHoy) {
        Partido partido = new Partido();
        partido.setTitulo(titulo);
        partido.setFechaHora(LocalDateTime.now().plusDays(diasDesdeHoy));
        partido.setZona(zona);
        partido.setDeporte(deporte);
        partido.setNivel(nivel);
        partido.setOrganizador(organizador);
        partido.setDuracionMinutos(90);
        
        return partido;
    }

    /**
     * Crear una zona de prueba (SOLO DEMO)
     */
    public static Zona crearZonaDemo(String barrio, String partido, double latitud, double longitud) {
        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setLatitud(latitud);
        ubicacion.setLongitud(longitud);
        
        return new Zona(barrio, partido, ubicacion);
    }

    /**
     * Crear un deporte de prueba (SOLO DEMO)
     */
    public static Deporte crearDeporteDemo(String nombre, String descripcion, int cantidadJugadores) {
        return new Deporte(nombre, descripcion, cantidadJugadores);
    }
} 