package com.tpo.unoMas;

import com.tpo.unoMas.model.*;
import com.tpo.unoMas.model.estado.NecesitamosJugadores;
import com.tpo.unoMas.model.strategy.emparejamiento.*;
import com.tpo.unoMas.model.strategy.notificacion.INotificacionStrategy;
import com.tpo.unoMas.service.InvitacionService;
import com.tpo.unoMas.service.NotificacionService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ObserverStrategyDemo {

    // Mock strategy simple para capturar notificaciones
    private static class SimpleNotificacionStrategy implements INotificacionStrategy {
        @Override
        public void enviarNotificacion(Notificacion notificacion) {
            // Simplemente imprimir
        }
    }

    public static void main(String[] args) {
        System.out.println("🎯 DEMO: PATRÓN OBSERVER + STRATEGY PARA INVITACIONES");
        System.out.println("=".repeat(60));
        
        // Configurar servicios
        NotificacionService notificacionService = new NotificacionService();
        InvitacionService invitacionService = new InvitacionService();
        SimpleNotificacionStrategy mockStrategy = new SimpleNotificacionStrategy();
        notificacionService.cambiarEstrategiaNotificacion(mockStrategy);
        
        // Crear jugadores de prueba
        List<Jugador> jugadores = crearJugadoresPrueba();
        invitacionService.registrarJugadores(jugadores);
        
        System.out.println("✅ Configuración inicial completada");
        System.out.println("👥 Jugadores registrados: " + jugadores.size());
        System.out.println("🎯 Estrategia por defecto: " + invitacionService.getEstrategiaDefecto().getDescripcion());
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🧪 CREANDO PARTIDO - INVITACIONES AUTOMÁTICAS");
        System.out.println("=".repeat(60));
        
        // Crear partido
        Partido partido = crearPartidoPrueba();
        
        // ✅ CLAVE: Registrar InvitacionService como Observer
        partido.attach(invitacionService);
        partido.attach(notificacionService);
        
        System.out.println("📝 Partido creado: " + partido.getTitulo());
        System.out.println("🔗 InvitacionService registrado como Observer");
        
        // ✅ DISPARAR EL EVENTO: Cambiar estado activa los observers
        System.out.println("\n🚀 CAMBIANDO ESTADO → Esto dispara las invitaciones automáticas");
        partido.cambiarEstado(new NecesitamosJugadores());
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🎯 PROBANDO DIFERENTES ESTRATEGIAS MANUALMENTE");
        System.out.println("=".repeat(60));
        
        // Probar estrategia por cercanía
        System.out.println("\n🌍 Estrategia por Cercanía:");
        EstrategiaEmparejamiento estrategiaCercania = new EmparejamientoPorCercania();
        List<Jugador> invitadosCercania = invitacionService.enviarInvitaciones(partido, jugadores, estrategiaCercania);
        System.out.println("   📧 Invitados: " + invitadosCercania.size());
        
        // Probar estrategia por nivel
        System.out.println("\n⭐ Estrategia por Nivel:");
        EstrategiaEmparejamiento estrategiaNivel = new EmparejamientoPorNivel();
        List<Jugador> invitadosNivel = invitacionService.enviarInvitaciones(partido, jugadores, estrategiaNivel);
        System.out.println("   📧 Invitados: " + invitadosNivel.size());
        
        // Probar estrategia por historial
        System.out.println("\n📊 Estrategia por Historial:");
        EstrategiaEmparejamiento estrategiaHistorial = new EmparejamientoPorHistorial();
        List<Jugador> invitadosHistorial = invitacionService.enviarInvitaciones(partido, jugadores, estrategiaHistorial);
        System.out.println("   📧 Invitados: " + invitadosHistorial.size());
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("✅ DEMOSTRACIÓN COMPLETADA");
        System.out.println("=".repeat(60));
        System.out.println("🔄 Observer Pattern: InvitacionService reacciona automáticamente a creación de partidos");
        System.out.println("🎯 Strategy Pattern: Se pueden usar diferentes algoritmos de emparejamiento");
        System.out.println("📧 Sistema integrado de invitaciones funcionando correctamente");
        
        mostrarInformacionJugadores(jugadores);
    }
    
    private static List<Jugador> crearJugadoresPrueba() {
        List<Jugador> jugadores = new ArrayList<>();
        
        // Crear zonas
        Zona zonaPalermo = new Zona("Palermo", "CABA", null);
        Zona zonaSanTelmo = new Zona("San Telmo", "CABA", null);
        
        // Crear deportes
        Deporte futbol = new Deporte("Fútbol 5", "Deporte de equipo");
        
        // Crear jugadores
        jugadores.add(crearJugador("Carlos", zonaPalermo, futbol, Nivel.INTERMEDIO));
        jugadores.add(crearJugador("María", zonaPalermo, futbol, Nivel.PRINCIPIANTE));
        jugadores.add(crearJugador("Diego", zonaSanTelmo, futbol, Nivel.INTERMEDIO));
        jugadores.add(crearJugador("Lucía", zonaSanTelmo, futbol, Nivel.AVANZADO));
        
        return jugadores;
    }
    
    private static Jugador crearJugador(String nombre, Zona zona, Deporte deporte, Nivel nivel) {
        Jugador jugador = new Jugador(nombre, nombre.toLowerCase() + "@email.com", "password123", zona);
        
        DeporteJugador deporteJugador = new DeporteJugador();
        deporteJugador.setJugador(jugador);
        deporteJugador.setDeporte(deporte);
        deporteJugador.setNivel(nivel);
        jugador.getDeportes().add(deporteJugador);
        
        return jugador;
    }
    
    private static Partido crearPartidoPrueba() {
        Zona zona = new Zona("Palermo", "CABA", null);
        Deporte futbol = new Deporte("Fútbol 5", "Deporte de equipo");
        Jugador organizador = new Jugador("Ana", "ana@email.com", "password123", zona);
        
        Partido partido = new Partido();
        partido.setTitulo("Fútbol de Sábado - Demo Observer + Strategy");
        partido.setFechaHora(LocalDateTime.now().plusDays(2));
        partido.setZona(zona);
        partido.setDeporte(futbol);
        partido.setNivel(Nivel.INTERMEDIO);
        partido.setMinJugadores(4);
        partido.setMaxJugadores(8);
        partido.setOrganizador(organizador);
        partido.setDuracionMinutos(90);
        
        return partido;
    }
    
    private static void mostrarInformacionJugadores(List<Jugador> jugadores) {
        System.out.println("\n👥 JUGADORES EN EL SISTEMA:");
        for (Jugador jugador : jugadores) {
            System.out.println("   🏃 " + jugador.getNombre() + 
                             " (Zona: " + jugador.getZona().getBarrio() + 
                             ", Nivel: " + jugador.getDeportes().get(0).getNivel() + ")");
        }
    }
} 