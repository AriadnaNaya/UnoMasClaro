package com.tpo.unoMas;

import com.tpo.unoMas.model.*;
import com.tpo.unoMas.model.estado.*;
import com.tpo.unoMas.model.strategy.INotificacionStrategy;
import com.tpo.unoMas.service.NotificacionService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NotificacionesValidationDemo {

    // Strategy simple para capturar notificaciones
    private static class SimpleNotificationStrategy implements INotificacionStrategy {
        private final List<String> notificaciones = new ArrayList<>();
        
        @Override
        public void enviarNotificacion(Notificacion notificacion) {
            String mensaje = String.format("📧 NOTIFICACIÓN: %s -> %s", 
                notificacion.getTitulo(), notificacion.getMensaje());
            notificaciones.add(mensaje);
            System.out.println(mensaje);
        }
        
        public List<String> getNotificaciones() {
            return new ArrayList<>(notificaciones);
        }
        
        public int getCantidadNotificaciones() {
            return notificaciones.size();
        }
    }

    public static void main(String[] args) {
        System.out.println("🔍 VALIDACIÓN DE NOTIFICACIONES POR CAMBIO DE ESTADO");
        System.out.println("====================================================");
        
        // Setup del sistema
        Partido partido = crearPartido();
        SimpleNotificationStrategy strategy = new SimpleNotificationStrategy();
        NotificacionService notificationService = new NotificacionService();
        notificationService.cambiarEstrategiaNotificacion(strategy);
        
        // Suscribir el observador
        partido.attach(notificationService);
        
        // Crear zona para los jugadores
        Zona zonaJugadores = crearZona();
        
        // Crear jugadores
        Jugador jugador1 = new Jugador("María", "maria@email.com", "password123", zonaJugadores);
        Jugador jugador2 = new Jugador("Carlos", "carlos@email.com", "password123", zonaJugadores);
        
        System.out.println("\n🎯 INICIANDO VALIDACIÓN...\n");
        
        // Test 1: Estado inicial - sin notificaciones
        mostrarEstado("INICIAL", partido, strategy);
        
        // Test 2: Agregar primer jugador - NO debe notificar (no cambia estado)
        System.out.println("▶️ Agregando primer jugador...");
        partido.agregarJugador(jugador1);
        mostrarEstado("PRIMER JUGADOR", partido, strategy);
        
        // Test 3: Agregar segundo jugador - DEBE notificar (cambio de estado)
        System.out.println("▶️ Agregando segundo jugador (alcanza mínimo)...");
        partido.agregarJugador(jugador2);
        mostrarEstado("SEGUNDO JUGADOR", partido, strategy);
        
        // Test 4: Confirmar asistencias - DEBE notificar (cambio de estado)
        System.out.println("▶️ Confirmando asistencia jugador 1...");
        partido.confirmarAsistencia(jugador1);
        mostrarEstado("CONFIRMACIÓN 1", partido, strategy);
        
        System.out.println("▶️ Confirmando asistencia jugador 2...");
        partido.confirmarAsistencia(jugador2);
        mostrarEstado("CONFIRMACIÓN 2", partido, strategy);
        
        // Test 5: Iniciar partido - DEBE notificar (cambio de estado)
        System.out.println("▶️ Iniciando partido...");
        partido.iniciar();
        mostrarEstado("PARTIDO INICIADO", partido, strategy);
        
        // Test 6: Finalizar partido - DEBE notificar (cambio de estado)
        System.out.println("▶️ Finalizando partido...");
        partido.finalizar();
        mostrarEstado("PARTIDO FINALIZADO", partido, strategy);
        
        // Resumen final
        System.out.println("\n" + "=".repeat(50));
        System.out.println("📊 RESUMEN DE VALIDACIÓN");
        System.out.println("=".repeat(50));
        System.out.println("Total de notificaciones enviadas: " + strategy.getCantidadNotificaciones());
        System.out.println("Notificaciones esperadas: 4 (PartidoArmado, Confirmado, EnJuego, Finalizado)");
        
        if (strategy.getCantidadNotificaciones() >= 4) {
            System.out.println("✅ VALIDACIÓN EXITOSA: Se generaron las notificaciones esperadas");
        } else {
            System.out.println("❌ ERROR: Faltan notificaciones");
        }
        
        System.out.println("\n🔍 DETALLE DE NOTIFICACIONES RECIBIDAS:");
        for (int i = 0; i < strategy.getNotificaciones().size(); i++) {
            System.out.println((i + 1) + ". " + strategy.getNotificaciones().get(i));
        }
    }
    
    private static void mostrarEstado(String momento, Partido partido, SimpleNotificationStrategy strategy) {
        System.out.println("📋 " + momento + ":");
        System.out.println("   Estado actual: " + partido.getEstado().getClass().getSimpleName());
        System.out.println("   Notificaciones totales: " + strategy.getCantidadNotificaciones());
        System.out.println();
    }
    
    private static Partido partido;
    
    private static Zona crearZona() {
        return new Zona("Palermo", "CABA", null);
    }
    
    private static Partido crearPartido() {
        // Crear entidades necesarias
        Zona zona = crearZona();
        Deporte deporte = new Deporte("Fútbol 5", "Deporte de equipo");
        
        // Crear partido
        partido = new Partido();
        partido.setTitulo("Validación de Notificaciones");
        partido.setFechaHora(LocalDate.now().plusDays(1).atTime(20, 0));
        partido.setZona(zona);
        partido.setDeporte(deporte);
        partido.setMinJugadores(2);
        partido.setMaxJugadores(4);
        
        return partido;
    }
} 