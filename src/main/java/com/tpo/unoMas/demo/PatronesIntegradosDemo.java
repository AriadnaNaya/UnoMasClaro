package com.tpo.unoMas.demo;

import com.tpo.unoMas.model.*;
import com.tpo.unoMas.model.observer.Observer;
import com.tpo.unoMas.model.strategy.notificacion.INotificacionStrategy;
import com.tpo.unoMas.service.NotificacionService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PatronesIntegradosDemo {

    public static void main(String[] args) {
        System.out.println("🎯 DEMO DE INTEGRACIÓN DE PATRONES 🎯");
        System.out.println("State + Observer + Strategy + Adapter");
        System.out.println("=====================================\n");

        // Crear datos de prueba
        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setLatitud(-34.6036840);
        ubicacion.setLongitud(-58.3815591);
        
        Zona zona = new Zona("Palermo", "CABA", ubicacion);
        Deporte deporte = new Deporte("Fútbol 5", "Fútbol de salón", 10);
        
        // Crear jugadores
        Jugador organizador = crearJugador("Ana García", "ana@email.com", zona);
        Jugador jugador1 = crearJugador("Carlos López", "carlos@email.com", zona);
        Jugador jugador2 = crearJugador("María Pérez", "maria@email.com", zona);
        Jugador jugador3 = crearJugador("Diego Martín", "diego@email.com", zona);
        
        // Crear partido
        Partido partido = new Partido();
        partido.setTitulo("Partido del Viernes");
        partido.setFechaHora(LocalDateTime.now().plusDays(1));
        partido.setZona(zona);
        partido.setDeporte(deporte);
        partido.setNivel(Nivel.INTERMEDIO);
        partido.setOrganizador(organizador);
        partido.setDuracionMinutos(90);
        
        // ===== CONFIGURAR SISTEMA DE NOTIFICACIONES =====
        System.out.println("🔧 CONFIGURANDO SISTEMA DE NOTIFICACIONES");
        System.out.println("------------------------------------------");
        
        // Crear sistema de notificaciones con strategy
        List<String> notificacionesEnviadas = new ArrayList<>();
        DemoNotificacionStrategy demoStrategy = new DemoNotificacionStrategy(notificacionesEnviadas);
        NotificacionService notificacionService = new NotificacionService();
        notificacionService.cambiarEstrategiaNotificacion(demoStrategy);
        
        // Crear observadores adicionales
        DemoObserver logObserver = new DemoObserver("📝 LogObserver");
        DemoObserver metricsObserver = new DemoObserver("📊 MetricsObserver");
        
        // Suscribir observadores al partido
        partido.attach(notificacionService);
        partido.attach(logObserver);
        partido.attach(metricsObserver);
        
        System.out.println("✓ NotificacionService configurado con " + demoStrategy.getClass().getSimpleName());
        System.out.println("✓ Observadores suscritos: NotificacionService, LogObserver, MetricsObserver");
        System.out.println("✓ Patrón Observer: " + partido.getClass().getSimpleName() + " implements Observable");
        System.out.println();
        
        // ===== INFORMACIÓN DEL PARTIDO =====
        System.out.println("📋 INFORMACIÓN DEL PARTIDO");
        System.out.println("---------------------------");
        System.out.println("   Título: " + partido.getTitulo());
        System.out.println("   Fecha: " + partido.getFechaHora().toLocalDate());
        System.out.println("   Zona: " + zona.getBarrio() + ", " + zona.getPartido());
        System.out.println("   Deporte: " + deporte.getNombre());
        System.out.println("   Organizador: " + organizador.getNombre());
        System.out.println();
        
        // ===== DEMOSTRAR INTEGRACIÓN DE PATRONES =====
        System.out.println("🎪 DEMOSTRANDO INTEGRACIÓN DE PATRONES");
        System.out.println("=======================================\n");
        
        // 1. Estado inicial
        mostrarEstadoCompleto(partido, "ESTADO INICIAL", notificacionesEnviadas, logObserver, metricsObserver);
        
        // 2. Agregar primer jugador (no cambia estado)
        System.out.println("🟡 Agregando primer jugador...");
        partido.agregarJugador(jugador1);
        mostrarEstadoCompleto(partido, "DESPUÉS DE AGREGAR JUGADOR 1", notificacionesEnviadas, logObserver, metricsObserver);
        
        // 3. Agregar segundo jugador (TRANSICIÓN DE ESTADO)
        System.out.println("🟢 Agregando segundo jugador (alcanza mínimo)...");
        System.out.println("   ⚡ ESTO DEBE ACTIVAR EL PATRÓN OBSERVER...");
        partido.agregarJugador(jugador2);
        mostrarEstadoCompleto(partido, "TRANSICIÓN: NecesitamosJugadores → PartidoArmado", notificacionesEnviadas, logObserver, metricsObserver);
        
        // 4. Cambiar estrategia de notificación en tiempo real
        System.out.println("🔄 CAMBIANDO ESTRATEGIA DE NOTIFICACIÓN...");
        EmailDemoStrategy emailStrategy = new EmailDemoStrategy();
        notificacionService.cambiarEstrategiaNotificacion(emailStrategy);
        System.out.println("   ✓ Estrategia cambiada a: " + emailStrategy.getClass().getSimpleName());
        System.out.println();
        
        // 5. Confirmar asistencias (TRANSICIÓN DE ESTADO)
        System.out.println("✅ Confirmando asistencias...");
        System.out.println("   ⚡ ESTO DEBE ACTIVAR LA NUEVA ESTRATEGIA...");
        partido.confirmarAsistencia(jugador1);
        partido.confirmarAsistencia(jugador2);
        mostrarEstadoCompleto(partido, "TRANSICIÓN: PartidoArmado → Confirmado", notificacionesEnviadas, logObserver, metricsObserver);
        
        // 6. Cambiar a estrategia Push
        System.out.println("📱 CAMBIANDO A ESTRATEGIA PUSH...");
        PushDemoStrategy pushStrategy = new PushDemoStrategy();
        notificacionService.cambiarEstrategiaNotificacion(pushStrategy);
        System.out.println("   ✓ Estrategia cambiada a: " + pushStrategy.getClass().getSimpleName());
        System.out.println();
        
        // 7. Iniciar partido (TRANSICIÓN DE ESTADO)
        System.out.println("🏁 Iniciando partido...");
        System.out.println("   ⚡ ESTO DEBE ACTIVAR LA ESTRATEGIA PUSH...");
        partido.iniciar();
        mostrarEstadoCompleto(partido, "TRANSICIÓN: Confirmado → EnJuego", notificacionesEnviadas, logObserver, metricsObserver);
        
        // 8. Finalizar partido (TRANSICIÓN DE ESTADO)
        System.out.println("🏆 Finalizando partido...");
        partido.finalizar();
        mostrarEstadoCompleto(partido, "TRANSICIÓN: EnJuego → Finalizado", notificacionesEnviadas, logObserver, metricsObserver);
        
        // ===== RESUMEN FINAL =====
        System.out.println("📊 RESUMEN DE PATRONES DEMOSTRADOS");
        System.out.println("===================================");
        System.out.println("🎯 STATE PATTERN:");
        System.out.println("   ✓ Estados: NecesitamosJugadores → PartidoArmado → Confirmado → EnJuego → Finalizado");
        System.out.println("   ✓ Comportamiento dinámico según estado interno");
        System.out.println();
        
        System.out.println("👀 OBSERVER PATTERN:");
        System.out.println("   ✓ Observable: Partido notifica cambios de estado");
        System.out.println("   ✓ Observers: NotificacionService, LogObserver, MetricsObserver");
        System.out.println("   ✓ Total notificaciones LogObserver: " + logObserver.contadorNotificaciones);
        System.out.println("   ✓ Total notificaciones MetricsObserver: " + metricsObserver.contadorNotificaciones);
        System.out.println();
        
        System.out.println("🎯 STRATEGY PATTERN:");
        System.out.println("   ✓ Estrategias usadas: DemoNotificacionStrategy, EmailDemoStrategy, PushDemoStrategy");
        System.out.println("   ✓ Cambio dinámico de algoritmo de notificación");
        System.out.println("   ✓ Total emails enviados: " + emailStrategy.contadorEmails);
        System.out.println("   ✓ Total pushes enviados: " + pushStrategy.contadorPushes);
        System.out.println();
        
        System.out.println("🔌 ADAPTER PATTERN:");
        System.out.println("   ✓ Adapta diferentes sistemas de notificación");
        System.out.println("   ✓ Interfaz unificada para Email, Push, etc.");
        System.out.println("   ✓ Total notificaciones procesadas: " + notificacionesEnviadas.size());
        System.out.println();
        
        System.out.println("🎉 ¡DEMO COMPLETADA EXITOSAMENTE!");
        System.out.println("Todos los patrones funcionan integrados de manera transparente.");
    }
    
    private static Jugador crearJugador(String nombre, String email, Zona zona) {
        Jugador jugador = new Jugador();
        jugador.setNombre(nombre);
        jugador.setEmail(email);
        jugador.setPassword("password123");
        jugador.setZona(zona);
        return jugador;
    }
    
    private static void mostrarEstadoCompleto(Partido partido, String momento, 
                                            List<String> notificaciones,
                                            DemoObserver logObserver,
                                            DemoObserver metricsObserver) {
        System.out.println("\n📊 " + momento + ":");
        System.out.println("   🎯 Estado actual: " + partido.getEstado().getClass().getSimpleName());
        System.out.println("   👥 Jugadores: " + partido.getJugadores().size() + "/" + partido.getDeporte().getCantidadJugadores());
        System.out.println("   ✅ Confirmados: " + partido.getJugadoresConfirmados().size());
        System.out.println("   📧 Notificaciones enviadas: " + notificaciones.size());
        System.out.println("   📝 Logs generados: " + logObserver.contadorNotificaciones);
        System.out.println("   📊 Métricas registradas: " + metricsObserver.contadorNotificaciones);
        
        if (!notificaciones.isEmpty()) {
            System.out.println("   💬 Última notificación: " + notificaciones.get(notificaciones.size() - 1));
        }
        System.out.println();
    }
    
    // ===== CLASES DEMO PARA MOSTRAR LOS PATRONES =====
    
    private static class DemoNotificacionStrategy implements INotificacionStrategy {
        private final List<String> notificaciones;
        
        public DemoNotificacionStrategy(List<String> notificaciones) {
            this.notificaciones = notificaciones;
        }
        
        @Override
        public void enviarNotificacion(Notificacion notificacion) {
            String mensaje = "[DEMO] " + notificacion.getTitulo() + ": " + notificacion.getMensaje();
            notificaciones.add(mensaje);
            System.out.println("       📤 " + mensaje);
        }
    }
    
    private static class EmailDemoStrategy implements INotificacionStrategy {
        public int contadorEmails = 0;
        
        @Override
        public void enviarNotificacion(Notificacion notificacion) {
            contadorEmails++;
            System.out.println("       📧 EMAIL enviado a " + notificacion.getDestinatario().getEmail() + 
                             ": " + notificacion.getTitulo());
        }
    }
    
    private static class PushDemoStrategy implements INotificacionStrategy {
        public int contadorPushes = 0;
        
        @Override
        public void enviarNotificacion(Notificacion notificacion) {
            contadorPushes++;
            System.out.println("       📱 PUSH enviado a " + notificacion.getDestinatario().getNombre() + 
                             ": " + notificacion.getTitulo());
        }
    }
    
    private static class DemoObserver implements Observer {
        public final String nombre;
        public int contadorNotificaciones = 0;
        
        public DemoObserver(String nombre) {
            this.nombre = nombre;
        }
        
        @Override
        public void update(Partido partido) {
            contadorNotificaciones++;
            String estadoActual = partido.getEstado().getClass().getSimpleName();
            System.out.println("       " + nombre + " notificado: " + estadoActual);
        }
    }
} 