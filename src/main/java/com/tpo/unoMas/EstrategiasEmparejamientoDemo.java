package com.tpo.unoMas;

import com.tpo.unoMas.model.*;
import com.tpo.unoMas.model.strategy.emparejamiento.*;
import com.tpo.unoMas.model.strategy.notificacion.INotificacionStrategy;
import com.tpo.unoMas.service.NotificacionService;
import com.tpo.unoMas.DemoInvitacionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EstrategiasEmparejamientoDemo {

    // Mock strategy para capturar notificaciones sin enviarlas realmente
    private static class DemoNotificacionStrategy implements INotificacionStrategy {
        private final List<String> notificacionesEnviadas = new ArrayList<>();
        
        @Override
        public void enviarNotificacion(Notificacion notificacion) {
            String mensaje = String.format("📧 [DEMO] %s para %s: %s", 
                notificacion.getTitulo(), 
                notificacion.getDestinatario().getNombre(),
                notificacion.getMensaje());
            notificacionesEnviadas.add(mensaje);
            System.out.println(mensaje);
        }
        
        public int getCantidadEnviadas() {
            return notificacionesEnviadas.size();
        }
    }

    public static void main(String[] args) {
        System.out.println("🎯 DEMO DE ESTRATEGIAS DE EMPAREJAMIENTO");
        System.out.println("Patrón Strategy aplicado a invitaciones de partidos");
        System.out.println("=".repeat(60));
        
        // Configurar datos de prueba
        DatosPrueba datos = new DatosPrueba();
        
        // Configurar servicios
        NotificacionService notificacionService = new NotificacionService();
        DemoNotificacionStrategy demoStrategy = new DemoNotificacionStrategy();
        notificacionService.cambiarEstrategiaNotificacion(demoStrategy);
        
        // Crear instancias de estrategias
        EmparejamientoPorCercania estrategiaCercania = new EmparejamientoPorCercania();
        EmparejamientoPorNivel estrategiaNivel = new EmparejamientoPorNivel();
        EmparejamientoPorHistorial estrategiaHistorial = new EmparejamientoPorHistorial();
        
        System.out.println("\n📋 DATOS DE LA DEMO:");
        System.out.println("🏆 Partido: " + datos.partidoFutbol.getTitulo());
        System.out.println("📍 Zona: " + datos.partidoFutbol.getZona().getBarrio() + ", " + datos.partidoFutbol.getZona().getPartido());
        System.out.println("⭐ Nivel: " + datos.partidoFutbol.getNivel());
        System.out.println("👥 Jugadores registrados: " + datos.jugadoresRegistrados.size());
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🧪 PROBANDO ESTRATEGIAS DE EMPAREJAMIENTO");
        System.out.println("=".repeat(60));
        
        // 1. Estrategia por Cercanía
        System.out.println("\n🌍 ESTRATEGIA POR CERCANÍA");
        System.out.println("Busca jugadores en la misma zona geográfica");
        List<Jugador> invitadosCercania = DemoInvitacionUtils.enviarInvitaciones(
            datos.partidoFutbol, datos.jugadoresRegistrados, estrategiaCercania, notificacionService);
        mostrarResultados("Cercanía", invitadosCercania);
        
        // 2. Estrategia por Nivel
        System.out.println("\n⭐ ESTRATEGIA POR NIVEL");
        System.out.println("Busca jugadores con nivel compatible");
        List<Jugador> invitadosNivel = DemoInvitacionUtils.enviarInvitaciones(
            datos.partidoFutbol, datos.jugadoresRegistrados, estrategiaNivel, notificacionService);
        mostrarResultados("Nivel", invitadosNivel);
        
        // 3. Estrategia por Historial
        System.out.println("\n📊 ESTRATEGIA POR HISTORIAL");
        System.out.println("Busca jugadores con historial relevante");
        List<Jugador> invitadosHistorial = DemoInvitacionUtils.enviarInvitaciones(
            datos.partidoFutbol, datos.jugadoresRegistrados, estrategiaHistorial, notificacionService);
        mostrarResultados("Historial", invitadosHistorial);
        
        // 4. Todas las estrategias
        System.out.println("\n🎯 TODAS LAS ESTRATEGIAS COMBINADAS");
        System.out.println("Para partidos prioritarios");
        List<Jugador> todosCombinados = DemoInvitacionUtils.enviarInvitacionesConTodasEstrategias(
            datos.partidoFutbol, datos.jugadoresRegistrados, notificacionService);
        mostrarResultados("Combinadas", todosCombinados);
        
        // Resumen final
        System.out.println("\n" + "=".repeat(60));
        System.out.println("📊 RESUMEN FINAL");
        System.out.println("=".repeat(60));
        System.out.println("🌍 Por Cercanía: " + invitadosCercania.size() + " invitaciones");
        System.out.println("⭐ Por Nivel: " + invitadosNivel.size() + " invitaciones");
        System.out.println("📊 Por Historial: " + invitadosHistorial.size() + " invitaciones");
        System.out.println("🎯 Combinadas (únicas): " + todosCombinados.size() + " invitaciones");
        System.out.println("📧 Total notificaciones enviadas: " + demoStrategy.getCantidadEnviadas());
        
        System.out.println("\n✅ PATRÓN STRATEGY IMPLEMENTADO EXITOSAMENTE");
        System.out.println("📋 Cada estrategia aplica un algoritmo diferente para encontrar jugadores");
        System.out.println("🔧 Se puede cambiar la estrategia dinámicamente según las necesidades");
    }
    
    private static void mostrarResultados(String estrategia, List<Jugador> invitados) {
        System.out.println("✅ Estrategia " + estrategia + ": " + invitados.size() + " jugadores invitados");
        if (!invitados.isEmpty()) {
            System.out.println("   Invitados: " + 
                invitados.stream()
                    .map(Jugador::getNombre)
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("Ninguno"));
        }
    }
    
    // Clase helper para crear datos de prueba
    private static class DatosPrueba {
        public Partido partidoFutbol;
        public List<Jugador> jugadoresRegistrados;
        
        public DatosPrueba() {
            crearDatos();
        }
        
        private void crearDatos() {
            // Crear zonas
            Zona zonaPalermo = new Zona("Palermo", "CABA", null);
            Zona zonaSanTelmo = new Zona("San Telmo", "CABA", null);
            Zona zonaLanus = new Zona("Lanús", "Lanús", null);
            
            // Crear deporte
            Deporte futbol = new Deporte("Fútbol 11", "Deporte de equipo", 22);
            
            // Crear organizador
            Jugador organizador = new Jugador("Ana", "ana@email.com", "password123", zonaPalermo);
            
            // Crear partido
            partidoFutbol = new Partido();
            partidoFutbol.setTitulo("Fútbol de Sábado");
            partidoFutbol.setFechaHora(LocalDateTime.now().plusDays(3));
            partidoFutbol.setZona(zonaPalermo);
            partidoFutbol.setDeporte(futbol);
            partidoFutbol.setNivel(Nivel.INTERMEDIO);
            partidoFutbol.setOrganizador(organizador);
            
            // Crear jugadores registrados
            jugadoresRegistrados = new ArrayList<>();
            
            // Jugadores en Palermo (misma zona) - nivel intermedio
            Jugador carlos = crearJugador("Carlos", "carlos@email.com", zonaPalermo, futbol, Nivel.INTERMEDIO);
            Jugador maría = crearJugador("María", "maria@email.com", zonaPalermo, futbol, Nivel.PRINCIPIANTE);
            jugadoresRegistrados.add(carlos);
            jugadoresRegistrados.add(maría);
            
            // Jugadores en San Telmo (CABA pero diferente barrio) - varios niveles
            Jugador diego = crearJugador("Diego", "diego@email.com", zonaSanTelmo, futbol, Nivel.INTERMEDIO);
            Jugador lucia = crearJugador("Lucía", "lucia@email.com", zonaSanTelmo, futbol, Nivel.AVANZADO);
            jugadoresRegistrados.add(diego);
            jugadoresRegistrados.add(lucia);
            
            // Jugadores en Lanús (zona diferente)
            Jugador pedro = crearJugador("Pedro", "pedro@email.com", zonaLanus, futbol, Nivel.PRINCIPIANTE);
            jugadoresRegistrados.add(pedro);
            
            // Agregar historial a algunos jugadores
            // Carlos ha jugado con Ana antes
            Partido partidoAnterior1 = new Partido();
            partidoAnterior1.setOrganizador(organizador);
            partidoAnterior1.setZona(zonaPalermo);
            carlos.getPartidosParticipados().add(partidoAnterior1);
            
            // Diego ha jugado en San Telmo antes
            Partido partidoAnterior2 = new Partido();
            partidoAnterior2.setZona(zonaSanTelmo);
            diego.getPartidosParticipados().add(partidoAnterior2);
        }
        
        private Jugador crearJugador(String nombre, String email, Zona zona, Deporte deporte, Nivel nivel) {
            Jugador jugador = new Jugador(nombre, email, "password123", zona);
            
            // Agregar deporte con nivel
            DeporteJugador deporteJugador = new DeporteJugador();
            deporteJugador.setJugador(jugador);
            deporteJugador.setDeporte(deporte);
            deporteJugador.setNivel(nivel);
            jugador.getDeportes().add(deporteJugador);
            
            return jugador;
        }
    }
} 