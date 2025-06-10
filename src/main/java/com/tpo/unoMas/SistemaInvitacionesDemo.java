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

public class SistemaInvitacionesDemo {

    // Mock strategy para capturar notificaciones sin enviarlas realmente
    private static class DemoNotificacionStrategy implements INotificacionStrategy {
        private int contadorNotificaciones = 0;
        
        @Override
        public void enviarNotificacion(Notificacion notificacion) {
            contadorNotificaciones++;
            System.out.println("📧 [NOTIFICACIÓN] " + notificacion.getTitulo() + 
                             " para " + notificacion.getDestinatario().getNombre());
        }
        
        public int getContadorNotificaciones() {
            return contadorNotificaciones;
        }
    }

    public static void main(String[] args) {
        System.out.println("🎯 DEMO DEL SISTEMA COMPLETO DE INVITACIONES");
        System.out.println("Observer + Strategy: Invitaciones automáticas al crear partidos");
        System.out.println("=".repeat(70));
        
        // Configurar servicios
        NotificacionService notificacionService = new NotificacionService();
        InvitacionService invitacionService = new InvitacionService();
        DemoNotificacionStrategy demoStrategy = new DemoNotificacionStrategy();
        notificacionService.cambiarEstrategiaNotificacion(demoStrategy);
        
        // Crear datos de prueba
        DatosPrueba datos = new DatosPrueba();
        
        // Registrar jugadores en el sistema de invitaciones
        invitacionService.registrarJugadores(datos.jugadoresRegistrados);
        
        System.out.println("\n📋 CONFIGURACIÓN INICIAL:");
        System.out.println("👥 Jugadores registrados: " + datos.jugadoresRegistrados.size());
        System.out.println("🎯 Estrategia por defecto: " + invitacionService.getEstrategiaDefecto().getDescripcion());
        
        System.out.println("\n" + "=".repeat(70));
        System.out.println("🧪 DEMO 1: CREACIÓN DE PARTIDO CON INVITACIONES AUTOMÁTICAS");
        System.out.println("=".repeat(70));
        
        // ✅ PASO 1: Crear partido y suscribir observers
        System.out.println("\n📝 PASO 1: Creando partido y registrando observers");
        Partido partido = datos.crearPartido();
        
        // Registrar AMBOS observers: NotificacionService + InvitacionService
        partido.attach(notificacionService);
        partido.attach(invitacionService);
        
        System.out.println("✅ Partido creado: " + partido.getTitulo());
        System.out.println("✅ Observers registrados: NotificacionService + InvitacionService");
        
        // ✅ PASO 2: Inicializar estado (esto disparará las invitaciones automáticas)
        System.out.println("\n🚀 PASO 2: Inicializando estado del partido (esto dispara invitaciones)");
        partido.cambiarEstado(new NecesitamosJugadores());
        
        System.out.println("\n" + "=".repeat(70));
        System.out.println("🧪 DEMO 2: CAMBIO DE ESTRATEGIA Y NUEVA CREACIÓN");
        System.out.println("=".repeat(70));
        
        // ✅ PASO 3: Cambiar estrategia por defecto
        System.out.println("\n🔧 PASO 3: Cambiando estrategia por defecto a 'Por Nivel'");
        invitacionService.setEstrategiaDefecto(new EmparejamientoPorNivel());
        
        // ✅ PASO 4: Crear segundo partido
        System.out.println("\n📝 PASO 4: Creando segundo partido con nueva estrategia");
        Partido partido2 = datos.crearSegundoPartido();
        partido2.attach(notificacionService);
        partido2.attach(invitacionService);
        partido2.cambiarEstado(new NecesitamosJugadores());
        
        System.out.println("\n" + "=".repeat(70));
        System.out.println("🧪 DEMO 3: INVITACIONES MANUALES CON ESTRATEGIA ESPECÍFICA");
        System.out.println("=".repeat(70));
        
        // ✅ PASO 5: Invitaciones manuales con estrategia específica
        System.out.println("\n🎯 PASO 5: Enviando invitaciones manuales con estrategia Por Historial");
        EstrategiaEmparejamiento estrategiaHistorial = new EmparejamientoPorHistorial();
        List<Jugador> invitadosHistorial = invitacionService.enviarInvitaciones(
            partido, datos.jugadoresRegistrados, estrategiaHistorial);
        
        System.out.println("\n" + "=".repeat(70));
        System.out.println("📊 RESUMEN FINAL DEL SISTEMA");
        System.out.println("=".repeat(70));
        System.out.println("🏆 Partidos creados: 2");
        System.out.println("👥 Jugadores registrados: " + datos.jugadoresRegistrados.size());
        System.out.println("📧 Total notificaciones enviadas: " + demoStrategy.getContadorNotificaciones());
        System.out.println("🎯 Invitados en última consulta manual: " + invitadosHistorial.size());
        
        System.out.println("\n✅ PATRÓN OBSERVER + STRATEGY FUNCIONANDO PERFECTAMENTE");
        System.out.println("🔄 InvitacionService actúa como Observer para invitaciones automáticas");
        System.out.println("🎯 Strategy Pattern permite cambiar algoritmos de emparejamiento dinámicamente");
        System.out.println("📧 Sistema integrado de notificaciones e invitaciones");
        
        mostrarJugadoresRegistrados(datos.jugadoresRegistrados);
    }
    
    private static void mostrarJugadoresRegistrados(List<Jugador> jugadores) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("👥 JUGADORES REGISTRADOS EN EL SISTEMA");
        System.out.println("=".repeat(70));
        
        for (Jugador jugador : jugadores) {
            String deportes = jugador.getDeportes().stream()
                .map(dj -> dj.getDeporte().getNombre() + " (" + dj.getNivel() + ")")
                .reduce((a, b) -> a + ", " + b)
                .orElse("Sin deportes");
            
            System.out.println("👤 " + jugador.getNombre() + 
                             " | Zona: " + jugador.getZona().getBarrio() + 
                             " | Deportes: " + deportes);
        }
    }
    
    // Clase helper para crear datos de prueba
    private static class DatosPrueba {
        public List<Jugador> jugadoresRegistrados;
        
        public DatosPrueba() {
            crearJugadores();
        }
        
        public Partido crearPartido() {
            // Crear zona y deporte
            Zona zonaPalermo = new Zona("Palermo", "CABA", null);
            Deporte futbol = new Deporte("Fútbol 5", "Deporte de equipo");
            
            // Crear organizador
            Jugador organizador = new Jugador("Ana", "ana@email.com", "password123", zonaPalermo);
            
            // Crear partido
            Partido partido = new Partido();
            partido.setTitulo("Fútbol de Sábado - Demo");
            partido.setFechaHora(LocalDateTime.now().plusDays(2));
            partido.setZona(zonaPalermo);
            partido.setDeporte(futbol);
            partido.setNivel(Nivel.INTERMEDIO);
            partido.setMinJugadores(4);
            partido.setMaxJugadores(8);
            partido.setOrganizador(organizador);
            
            return partido;
        }
        
        public Partido crearSegundoPartido() {
            // Crear zona y deporte diferentes
            Zona zonaSanTelmo = new Zona("San Telmo", "CABA", null);
            Deporte basquet = new Deporte("Básquet", "Deporte de equipo");
            
            // Crear organizador
            Jugador organizador = new Jugador("Diego", "diego@email.com", "password123", zonaSanTelmo);
            
            // Crear partido
            Partido partido = new Partido();
            partido.setTitulo("Básquet de Domingo - Demo");
            partido.setFechaHora(LocalDateTime.now().plusDays(4));
            partido.setZona(zonaSanTelmo);
            partido.setDeporte(basquet);
            partido.setNivel(Nivel.AVANZADO);
            partido.setMinJugadores(6);
            partido.setMaxJugadores(10);
            partido.setOrganizador(organizador);
            
            return partido;
        }
        
        private void crearJugadores() {
            jugadoresRegistrados = new ArrayList<>();
            
            // Crear zonas
            Zona zonaPalermo = new Zona("Palermo", "CABA", null);
            Zona zonaSanTelmo = new Zona("San Telmo", "CABA", null);
            Zona zonaLanus = new Zona("Lanús", "Lanús", null);
            
            // Crear deportes
            Deporte futbol = new Deporte("Fútbol 5", "Deporte de equipo");
            Deporte basquet = new Deporte("Básquet", "Deporte de equipo");
            
            // Jugadores en Palermo
            jugadoresRegistrados.add(crearJugador("Carlos", "carlos@email.com", zonaPalermo, futbol, Nivel.INTERMEDIO));
            jugadoresRegistrados.add(crearJugador("María", "maria@email.com", zonaPalermo, futbol, Nivel.PRINCIPIANTE));
            jugadoresRegistrados.add(crearJugador("Luis", "luis@email.com", zonaPalermo, basquet, Nivel.AVANZADO));
            
            // Jugadores en San Telmo
            jugadoresRegistrados.add(crearJugador("Diego", "diego@email.com", zonaSanTelmo, futbol, Nivel.INTERMEDIO));
            jugadoresRegistrados.add(crearJugador("Lucía", "lucia@email.com", zonaSanTelmo, basquet, Nivel.AVANZADO));
            jugadoresRegistrados.add(crearJugador("Pablo", "pablo@email.com", zonaSanTelmo, futbol, Nivel.AVANZADO));
            
            // Jugadores en Lanús
            jugadoresRegistrados.add(crearJugador("Pedro", "pedro@email.com", zonaLanus, futbol, Nivel.PRINCIPIANTE));
            jugadoresRegistrados.add(crearJugador("Sofia", "sofia@email.com", zonaLanus, basquet, Nivel.INTERMEDIO));
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