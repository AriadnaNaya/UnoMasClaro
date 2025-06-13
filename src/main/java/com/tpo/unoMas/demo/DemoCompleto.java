package com.tpo.unoMas.demo;

import com.tpo.unoMas.model.*;
import com.tpo.unoMas.model.adapter.AdapterJavaMail;
import com.tpo.unoMas.model.adapter.NotificacionEmailAdapter;
import com.tpo.unoMas.model.estado.*;
import com.tpo.unoMas.model.strategy.emparejamiento.*;
import com.tpo.unoMas.model.strategy.notificacion.*;
import com.tpo.unoMas.model.observer.Observer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * DEMO COMPLETO DE LA APLICACIÓN UNO MÁS
 * 
 * Ejecuta desde consola todos los patrones de diseño y funcionalidades:
 * - State Pattern (Estados de partido)
 * - Strategy Pattern (Emparejamiento y Notificaciones)
 * - Observer Pattern (Notificaciones automáticas)
 * - Adapter Pattern (Adaptación de sistemas externos)
 * 
 * Para ejecutar: java -jar target/unoMas-0.0.1-SNAPSHOT.jar --demo
 */
@Component
public class DemoCompleto implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        if (Arrays.asList(args).contains("--demo")) {
            System.out.println("=".repeat(80));
            System.out.println("🎯 DEMO COMPLETO - APLICACIÓN UNO MÁS");
            System.out.println("=".repeat(80));
            
            ejecutarDemoCompleto();
        }
    }

    public void ejecutarDemoCompleto() {
        System.out.println("\n📋 INICIANDO DEMO COMPLETO...");
        
        try {
            // 1. Demo de creación de datos básicos
            demoCreacionDatos();
            
            // 2. Demo de gestión de jugadores
            demoGestionJugadores();
            
            // 3. Demo de State Pattern (Estados de partido)
            demoStatePattern();
            
            // 4. Demo de Strategy Pattern (Emparejamiento)
            demoStrategyPatternEmparejamiento();
            
            // 5. Demo de Strategy Pattern (Notificaciones)
            demoStrategyPatternNotificaciones();
            
            // 6. Demo de Observer Pattern
            demoObserverPattern();
            
            // 7. Demo de flujo completo de partido
            demoFlujoCompletoPartido();
            
            System.out.println("\n✅ DEMO COMPLETO FINALIZADO");
            System.out.println("=".repeat(80));
        } catch (Exception e) {
            System.err.println("❌ Error en demo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void demoCreacionDatos() {
        System.out.println("\n🏗️  DEMO 1: CREACIÓN DE DATOS BÁSICOS");
        System.out.println("-".repeat(50));
        
        // Crear zonas
        Zona palermo = new Zona();
        palermo.setBarrio("Palermo");
        palermo.setPartido("CABA");
        palermo.setUbicacion(new Ubicacion(-34.5875, -58.4217));
        
        Zona belgrano = new Zona();
        belgrano.setBarrio("Belgrano");
        belgrano.setPartido("CABA");
        belgrano.setUbicacion(new Ubicacion(-34.5631, -58.4550));
        
        System.out.println("📍 Zonas creadas:");
        System.out.println("   - " + palermo.getBarrio() + " (" + palermo.getPartido() + ")");
        System.out.println("   - " + belgrano.getBarrio() + " (" + belgrano.getPartido() + ")");
        
        // Crear deportes
        Deporte futbol = new Deporte();
        futbol.setNombre("Fútbol 11");
        futbol.setDescripcion("Deporte de equipo");
        futbol.setCantidadJugadores(22);
        
        Deporte tenis = new Deporte();
        tenis.setNombre("Tenis");
        tenis.setDescripcion("Deporte de raqueta");
        tenis.setCantidadJugadores(2);
        
        System.out.println("⚽ Deportes creados:");
        System.out.println("   - " + futbol.getNombre() + " (" + futbol.getCantidadJugadores() + " jugadores)");
        System.out.println("   - " + tenis.getNombre() + " (" + tenis.getCantidadJugadores() + " jugadores)");
        
        System.out.println("✅ Datos básicos creados exitosamente");
    }

    private void demoGestionJugadores() {
        System.out.println("\n👥 DEMO 2: GESTIÓN DE JUGADORES");
        System.out.println("-".repeat(50));
        
        // Crear zona y deporte para los jugadores
        Zona zona = new Zona();
        zona.setBarrio("Palermo");
        zona.setPartido("CABA");
        zona.setUbicacion(new Ubicacion(-34.5875, -58.4217));
        
        Deporte futbol = new Deporte();
        futbol.setNombre("Fútbol 11");
        futbol.setDescripcion("Deporte de equipo");
        futbol.setCantidadJugadores(22);
        
        // Crear jugadores
        Jugador juan = new Jugador("Juan Pérez", "juan@email.com", "password123", zona);
        Jugador maria = new Jugador("María García", "maria@email.com", "password123", zona);
        Jugador carlos = new Jugador("Carlos López", "carlos@email.com", "password123", zona);
        
        System.out.println("👤 Jugadores creados:");
        System.out.println("   - " + juan.getNombre() + " (" + juan.getEmail() + ")");
        System.out.println("   - " + maria.getNombre() + " (" + maria.getEmail() + ")");
        System.out.println("   - " + carlos.getNombre() + " (" + carlos.getEmail() + ")");
        
        // Demo de deportes favoritos
        System.out.println("\n⭐ Demo de deportes favoritos:");
        
        // Agregar deporte a juan
        DeporteJugador dj = new DeporteJugador();
        dj.setJugador(juan);
        dj.setDeporte(futbol);
        dj.setNivel(Nivel.INTERMEDIO);
        dj.setEsFavorito(true);
        juan.getDeportes().add(dj);
        
        System.out.println("   - " + juan.getNombre() + " agregó " + futbol.getNombre() + " a favoritos");
        System.out.println("   - Deportes favoritos de " + juan.getNombre() + ": " + juan.deportesFavoritos().size());
        
        juan.eliminarDeFavoritos(futbol);
        System.out.println("   - " + juan.getNombre() + " eliminó " + futbol.getNombre() + " de favoritos");
        System.out.println("   - Deportes favoritos de " + juan.getNombre() + ": " + juan.deportesFavoritos().size());
        
        System.out.println("✅ Gestión de jugadores completada");
    }

    private void demoStatePattern() {
        System.out.println("\n🎭 DEMO 3: STATE PATTERN - ESTADOS DE PARTIDO");
        System.out.println("-".repeat(50));
        
        // Crear partido para demostrar estados
        Zona zona = new Zona();
        zona.setBarrio("Palermo");
        zona.setPartido("CABA");
        zona.setUbicacion(new Ubicacion(-34.5875, -58.4217));
        
        Deporte futbol = new Deporte();
        futbol.setNombre("Fútbol 11");
        futbol.setDescripcion("Deporte de equipo");
        futbol.setCantidadJugadores(22);
        
        Jugador organizador = new Jugador("Organizador", "org@email.com", "password123", zona);
        
        Partido partido = new Partido();
        partido.setTitulo("Partido Demo");
        partido.setFechaHora(LocalDateTime.now().plusDays(1));
        partido.setZona(zona);
        partido.setDeporte(futbol);
        partido.setOrganizador(organizador);
        partido.setDuracionMinutos(90);
        
        System.out.println("⚽ Partido creado: " + partido.getTitulo());
        
        // Demostrar transiciones de estado
        System.out.println("\n🔄 Demostrando transiciones de estado:");
        
        // Estado inicial
        System.out.println("1. Estado inicial: " + partido.getEstado().getClass().getSimpleName());
        
        // Transición a PartidoArmado
        partido.cambiarEstado(new PartidoArmado());
        System.out.println("2. Transición a: " + partido.getEstado().getClass().getSimpleName());
        
        // Transición a Confirmado
        partido.cambiarEstado(new Confirmado());
        System.out.println("3. Transición a: " + partido.getEstado().getClass().getSimpleName());
        
        // Transición a EnJuego
        partido.cambiarEstado(new EnJuego());
        System.out.println("4. Transición a: " + partido.getEstado().getClass().getSimpleName());
        
        // Transición a Finalizado
        partido.cambiarEstado(new Finalizado());
        System.out.println("5. Transición a: " + partido.getEstado().getClass().getSimpleName());
        
        // Demostrar cancelación
        Partido partidoCancelable = new Partido();
        partidoCancelable.setTitulo("Partido Cancelable");
        partidoCancelable.setFechaHora(LocalDateTime.now().plusDays(1));
        partidoCancelable.setZona(zona);
        partidoCancelable.setDeporte(futbol);
        partidoCancelable.setOrganizador(organizador);
        partidoCancelable.setDuracionMinutos(90);
        
        partidoCancelable.cambiarEstado(new PartidoArmado());
        System.out.println("\n❌ Demo de cancelación:");
        System.out.println("   - Estado antes: " + partidoCancelable.getEstado().getClass().getSimpleName());
        partidoCancelable.cancelar();
        System.out.println("   - Estado después: " + partidoCancelable.getEstado().getClass().getSimpleName());
        
        System.out.println("✅ State Pattern demostrado exitosamente");
    }

    private void demoStrategyPatternEmparejamiento() {
        System.out.println("\n🎯 DEMO 4: STRATEGY PATTERN - EMPAREJAMIENTO");
        System.out.println("-".repeat(50));
        
        // Crear datos para demo
        Zona zonaCerca = new Zona();
        zonaCerca.setBarrio("Palermo");
        zonaCerca.setPartido("CABA");
        zonaCerca.setUbicacion(new Ubicacion(-34.5875, -58.4217));
        
        Zona zonaLejos = new Zona();
        zonaLejos.setBarrio("Belgrano");
        zonaLejos.setPartido("CABA");
        zonaLejos.setUbicacion(new Ubicacion(-34.5631, -58.4550));
        
        Deporte futbol = new Deporte();
        futbol.setNombre("Fútbol 11");
        futbol.setDescripcion("Deporte de equipo");
        futbol.setCantidadJugadores(22);
        
        Jugador organizador = new Jugador("Organizador", "org@email.com", "password123", zonaCerca);
        
        Partido partido = new Partido();
        partido.setTitulo("Partido Demo");
        partido.setFechaHora(LocalDateTime.now().plusDays(1));
        partido.setZona(zonaCerca);
        partido.setDeporte(futbol);
        partido.setOrganizador(organizador);
        partido.setDuracionMinutos(90);
        partido.setNivel(Nivel.INTERMEDIO);
        
        // Crear jugadores con diferentes características
        Jugador jugadorCerca = new Jugador("Jugador Cerca", "cerca@email.com", "password123", zonaCerca);
        Jugador jugadorLejos = new Jugador("Jugador Lejos", "lejos@email.com", "password123", zonaLejos);
        Jugador jugadorPrincipiante = new Jugador("Jugador Principiante", "principiante@email.com", "password123", zonaCerca);
        Jugador jugadorAvanzado = new Jugador("Jugador Avanzado", "avanzado@email.com", "password123", zonaCerca);
        
        // Configurar niveles
        DeporteJugador djPrincipiante = new DeporteJugador();
        djPrincipiante.setJugador(jugadorPrincipiante);
        djPrincipiante.setDeporte(futbol);
        djPrincipiante.setNivel(Nivel.PRINCIPIANTE);
        jugadorPrincipiante.getDeportes().add(djPrincipiante);
        
        DeporteJugador djAvanzado = new DeporteJugador();
        djAvanzado.setJugador(jugadorAvanzado);
        djAvanzado.setDeporte(futbol);
        djAvanzado.setNivel(Nivel.AVANZADO);
        jugadorAvanzado.getDeportes().add(djAvanzado);
        
        List<Jugador> jugadoresDisponibles = Arrays.asList(
            jugadorCerca, jugadorLejos, jugadorPrincipiante, jugadorAvanzado
        );
        
        // Demo de estrategia por cercanía
        System.out.println("\n📍 Estrategia por Cercanía:");
        EstrategiaEmparejamiento estrategiaCercania = new EmparejamientoPorCercania();
        List<Jugador> resultadosCercania = estrategiaCercania.encontrarJugadoresPotenciales(partido, jugadoresDisponibles);
        System.out.println("   - Jugadores encontrados: " + resultadosCercania.size());
        resultadosCercania.forEach(j -> System.out.println("     * " + j.getNombre() + " (" + j.getZona().getBarrio() + ")"));
        
        // Demo de estrategia por nivel
        System.out.println("\n📊 Estrategia por Nivel:");
        EstrategiaEmparejamiento estrategiaNivel = new EmparejamientoPorNivel();
        List<Jugador> resultadosNivel = estrategiaNivel.encontrarJugadoresPotenciales(partido, jugadoresDisponibles);
        System.out.println("   - Jugadores encontrados: " + resultadosNivel.size());
        resultadosNivel.forEach(j -> {
            Nivel nivelJugador = j.getDeportes().isEmpty() ? Nivel.PRINCIPIANTE : 
                j.getDeportes().get(0).getNivel();
            System.out.println("     * " + j.getNombre() + " (Nivel: " + nivelJugador + ")");
        });
        
        // Demo de estrategia por historial
        System.out.println("\n📚 Estrategia por Historial:");
        EstrategiaEmparejamiento estrategiaHistorial = new EmparejamientoPorHistorial();
        List<Jugador> resultadosHistorial = estrategiaHistorial.encontrarJugadoresPotenciales(partido, jugadoresDisponibles);
        System.out.println("   - Jugadores encontrados: " + resultadosHistorial.size());
        
        // Demo de cambio dinámico de estrategia
        System.out.println("\n🔄 Cambio dinámico de estrategia:");
        partido.cambiarEstrategiaEmparejamiento(estrategiaCercania);
        System.out.println("   - Estrategia actual: " + partido.getEstrategiaEmparejamiento().getClass().getSimpleName());
        
        partido.cambiarEstrategiaEmparejamiento(estrategiaNivel);
        System.out.println("   - Estrategia cambiada a: " + partido.getEstrategiaEmparejamiento().getClass().getSimpleName());
        
        System.out.println("✅ Strategy Pattern de emparejamiento demostrado");
    }

    private void demoStrategyPatternNotificaciones() {
        System.out.println("\n📧 DEMO 5: STRATEGY PATTERN - NOTIFICACIONES");
        System.out.println("-".repeat(50));
        
        // Crear jugador para notificaciones
        Zona zona = new Zona();
        zona.setBarrio("Palermo");
        zona.setPartido("CABA");
        zona.setUbicacion(new Ubicacion(-34.5875, -58.4217));
        
        Deporte futbol = new Deporte();
        futbol.setNombre("Fútbol 11");
        futbol.setDescripcion("Deporte de equipo");
        futbol.setCantidadJugadores(22);
        
        Jugador jugador = new Jugador("Juan Test", "juan.test@email.com", "password123", zona);
        jugador.setTokenFCM("test-fcm-token");
        
        Notificacion notificacion = new Notificacion(
            "Invitación a partido",
            "Te invitamos a unirte al partido de fútbol en Palermo",
            jugador
        );
        
        // Demo de estrategia de Email
        System.out.println("📧 Estrategia de Email:");
        NotificacionEmailAdapter javaMailAdapter = new AdapterJavaMail();
        INotificacionStrategy estrategiaEmail = new NotificacionEmail(javaMailAdapter);
        try {
            estrategiaEmail.enviarNotificacion(notificacion);
            System.out.println("   ✅ Email enviado exitosamente a " + jugador.getEmail());
        } catch (Exception e) {
            System.out.println("   ⚠️  Email simulado (modo demo): " + e.getMessage());
        }
        
        // Demo de estrategia Push Firebase
        System.out.println("\n📱 Estrategia Push Firebase:");
        INotificacionStrategy estrategiaPush = new NotificacionPushFirebase();
        try {
            estrategiaPush.enviarNotificacion(notificacion);
            System.out.println("   ✅ Push notification enviada exitosamente");
        } catch (Exception e) {
            System.out.println("   ⚠️  Push notification simulada (modo demo): " + e.getMessage());
        }
        
        System.out.println("✅ Strategy Pattern de notificaciones demostrado");
    }

    private void demoObserverPattern() {
        System.out.println("\n👁️  DEMO 6: OBSERVER PATTERN");
        System.out.println("-".repeat(50));
        
        // Crear partido observable
        Zona zona = new Zona();
        zona.setBarrio("Palermo");
        zona.setPartido("CABA");
        zona.setUbicacion(new Ubicacion(-34.5875, -58.4217));
        
        Deporte futbol = new Deporte();
        futbol.setNombre("Fútbol 11");
        futbol.setDescripcion("Deporte de equipo");
        futbol.setCantidadJugadores(22);
        
        Jugador organizador = new Jugador("Organizador", "org@email.com", "password123", zona);
        
        Partido partido = new Partido();
        partido.setTitulo("Partido Observable");
        partido.setFechaHora(LocalDateTime.now().plusDays(1));
        partido.setZona(zona);
        partido.setDeporte(futbol);
        partido.setOrganizador(organizador);
        partido.setDuracionMinutos(90);
        
        // Crear observadores simulados
        Observer observador1 = new Observer() {
            @Override
            public void update(Partido partido) {
                System.out.println("   🔔 Observador 1: Cambio detectado en el partido - " + partido.getTitulo());
            }
        };
        
        Observer observador2 = new Observer() {
            @Override
            public void update(Partido partido) {
                System.out.println("   🔔 Observador 2: Notificando cambio de estado a " + partido.getEstado().getClass().getSimpleName());
            }
        };
        
        // Suscribir observadores
        partido.attach(observador1);
        partido.attach(observador2);
        System.out.println("👥 Observadores suscritos al partido");
        
        // Demostrar notificaciones automáticas al cambiar estado
        System.out.println("\n🔄 Cambios de estado y notificaciones automáticas:");
        
        partido.cambiarEstado(new PartidoArmado());
        esperar(100);
        
        partido.cambiarEstado(new Confirmado());
        esperar(100);
        
        partido.cambiarEstado(new EnJuego());
        esperar(100);
        
        // Desuscribir un observador
        partido.detach(observador1);
        System.out.println("\n📤 Observador 1 desuscrito");
        
        partido.cambiarEstado(new Finalizado());
        
        System.out.println("✅ Observer Pattern demostrado exitosamente");
    }

    private void demoFlujoCompletoPartido() {
        System.out.println("\n🎮 DEMO 7: FLUJO COMPLETO DE PARTIDO");
        System.out.println("-".repeat(50));
        
        System.out.println("🎯 Simulando un flujo completo de partido con todos los patrones...");
        
        // 1. Crear datos básicos
        Zona zona = new Zona();
        zona.setBarrio("Palermo");
        zona.setPartido("CABA");
        zona.setUbicacion(new Ubicacion(-34.5875, -58.4217));
        
        Deporte futbol = new Deporte();
        futbol.setNombre("Fútbol 11");
        futbol.setDescripcion("Deporte de equipo");
        futbol.setCantidadJugadores(22);
        
        // 2. Crear organizador
        Jugador organizador = new Jugador("Organizador", "org@email.com", "password123", zona);
        
        // 3. Crear partido (State Pattern - Estado inicial)
        Partido partido = new Partido();
        partido.setTitulo("Partido Final Demo");
        partido.setFechaHora(LocalDateTime.now().plusDays(1));
        partido.setZona(zona);
        partido.setDeporte(futbol);
        partido.setOrganizador(organizador);
        partido.setDuracionMinutos(90);
        partido.setNivel(Nivel.INTERMEDIO);
        
        System.out.println("1️⃣  Partido creado en estado: " + partido.getEstado().getClass().getSimpleName());
        
        // 4. Agregar organizador al partido
        partido.agregarJugador(organizador);
        System.out.println("2️⃣  Organizador agregado al partido");
        
        // 5. Buscar jugadores compatibles (Strategy Pattern)
        Jugador jugador1 = new Jugador("Jugador 1", "j1@email.com", "password123", zona);
        Jugador jugador2 = new Jugador("Jugador 2", "j2@email.com", "password123", zona);
        Jugador jugador3 = new Jugador("Jugador 3", "j3@email.com", "password123", zona);
        
        // Configurar niveles
        DeporteJugador dj1 = new DeporteJugador();
        dj1.setJugador(jugador1);
        dj1.setDeporte(futbol);
        dj1.setNivel(Nivel.INTERMEDIO);
        jugador1.getDeportes().add(dj1);
        
        DeporteJugador dj2 = new DeporteJugador();
        dj2.setJugador(jugador2);
        dj2.setDeporte(futbol);
        dj2.setNivel(Nivel.INTERMEDIO);
        jugador2.getDeportes().add(dj2);
        
        DeporteJugador dj3 = new DeporteJugador();
        dj3.setJugador(jugador3);
        dj3.setDeporte(futbol);
        dj3.setNivel(Nivel.AVANZADO);
        jugador3.getDeportes().add(dj3);
        
        List<Jugador> jugadoresDisponibles = Arrays.asList(jugador1, jugador2, jugador3);
        
        partido.cambiarEstrategiaEmparejamiento(new EmparejamientoPorNivel());
        List<Jugador> invitados = partido.matchearJugadores(jugadoresDisponibles);
        System.out.println("3️⃣  Jugadores matcheados usando estrategia por nivel: " + invitados.size());
        
        // 6. Simular que algunos jugadores se unen
        for (Jugador jugador : invitados.subList(0, Math.min(2, invitados.size()))) {
            partido.agregarJugador(jugador);
            System.out.println("    👤 " + jugador.getNombre() + " se unió al partido");
        }
        
        // 7. Transición de estado a PartidoArmado
        partido.cambiarEstado(new PartidoArmado());
        System.out.println("4️⃣  Estado cambiado a: " + partido.getEstado().getClass().getSimpleName());
        
        // 8. Confirmar asistencias
        for (Jugador jugador : partido.getJugadores()) {
            partido.confirmarAsistencia(jugador);
            System.out.println("    ✅ " + jugador.getNombre() + " confirmó asistencia");
        }
        
        // 9. Transición a Confirmado
        partido.cambiarEstado(new Confirmado());
        System.out.println("5️⃣  Estado cambiado a: " + partido.getEstado().getClass().getSimpleName());
        
        // 10. Iniciar partido
        partido.iniciar();
        System.out.println("6️⃣  Partido iniciado");
        
        // 11. Finalizar partido
        esperar(100);
        partido.finalizar();
        System.out.println("7️⃣  Partido finalizado");
        
        // 12. Resumen final
        System.out.println("\n📊 RESUMEN DEL PARTIDO:");
        System.out.println("   - Título: " + partido.getTitulo());
        System.out.println("   - Jugadores: " + partido.getJugadores().size());
        System.out.println("   - Confirmados: " + partido.getJugadoresConfirmados().size());
        System.out.println("   - Estado final: " + partido.getEstado().getClass().getSimpleName());
        System.out.println("   - Zona: " + partido.getZona().getBarrio());
        System.out.println("   - Deporte: " + partido.getDeporte().getNombre());
        
        System.out.println("✅ Flujo completo de partido demostrado exitosamente");
    }
    
    private void esperar(int milisegundos) {
        try {
            Thread.sleep(milisegundos);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
} 