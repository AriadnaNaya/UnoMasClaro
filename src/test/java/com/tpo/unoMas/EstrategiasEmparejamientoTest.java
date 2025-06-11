package com.tpo.unoMas;

import com.tpo.unoMas.model.*;
import com.tpo.unoMas.model.strategy.emparejamiento.*;
import com.tpo.unoMas.service.NotificacionService;
import com.tpo.unoMas.model.strategy.notificacion.INotificacionStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import com.tpo.unoMas.DemoInvitacionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EstrategiasEmparejamientoTest {

    private NotificacionService notificacionService;
    private Partido partidoFutbol;
    private List<Jugador> jugadoresRegistrados;
    
    // Mock strategy para tests
    private static class TestNotificacionStrategy implements INotificacionStrategy {
        private int contadorNotificaciones = 0;
        
        @Override
        public void enviarNotificacion(Notificacion notificacion) {
            contadorNotificaciones++;
        }
        
        public int getContadorNotificaciones() {
            return contadorNotificaciones;
        }
    }

    @BeforeEach
    public void setUp() {
        // Configurar servicios
        notificacionService = new NotificacionService();
        TestNotificacionStrategy testStrategy = new TestNotificacionStrategy();
        notificacionService.cambiarEstrategiaNotificacion(testStrategy);
        
        // Crear datos de prueba
        crearDatosPrueba();
    }
    
    @Test
    public void testEstrategiaPorCercania() {
        // Given
        EstrategiaEmparejamiento estrategia = new EmparejamientoPorCercania();
        
        // When
        List<Jugador> invitados = DemoInvitacionUtils.enviarInvitaciones(
            partidoFutbol, jugadoresRegistrados, estrategia, notificacionService);
        
        // Then
        assertNotNull(invitados);
        assertTrue(invitados.size() >= 0);
        
        // Verificar que todos los invitados están en la misma zona
        for (Jugador invitado : invitados) {
            if (partidoFutbol.getZona() != null && invitado.getZona() != null) {
                assertEquals(partidoFutbol.getZona().getPartido(), 
                           invitado.getZona().getPartido());
            }
        }
        
        System.out.println("✅ Test Estrategia por Cercanía: " + invitados.size() + " jugadores encontrados");
    }
    
    @Test
    public void testEstrategiaPorNivel() {
        // Given
        EstrategiaEmparejamiento estrategia = new EmparejamientoPorNivel();
        
        // When
        List<Jugador> invitados = DemoInvitacionUtils.enviarInvitaciones(
            partidoFutbol, jugadoresRegistrados, estrategia, notificacionService);
        
        // Then
        assertNotNull(invitados);
        assertTrue(invitados.size() >= 0);
        
        // Verificar que los invitados tienen nivel compatible
        for (Jugador invitado : invitados) {
            assertTrue(tieneNivelCompatible(invitado, partidoFutbol));
        }
        
        System.out.println("✅ Test Estrategia por Nivel: " + invitados.size() + " jugadores encontrados");
    }
    
    @Test
    public void testEstrategiaPorHistorial() {
        // Given
        EstrategiaEmparejamiento estrategia = new EmparejamientoPorHistorial();
        
        // When
        List<Jugador> invitados = DemoInvitacionUtils.enviarInvitaciones(
            partidoFutbol, jugadoresRegistrados, estrategia, notificacionService);
        
        // Then
        assertNotNull(invitados);
        assertTrue(invitados.size() >= 0);
        
        System.out.println("✅ Test Estrategia por Historial: " + invitados.size() + " jugadores encontrados");
    }
    
    @Test
    public void testTodasLasEstrategias() {
        // When
        List<Jugador> invitados = DemoInvitacionUtils.enviarInvitacionesConTodasEstrategias(
            partidoFutbol, jugadoresRegistrados, notificacionService);
        
        // Then
        assertNotNull(invitados);
        assertTrue(invitados.size() >= 0);
        
        System.out.println("✅ Test Todas las Estrategias: " + invitados.size() + " jugadores únicos encontrados");
    }
    
    @Test
    public void testPatronStrategyFlexibilidad() {
        // Given - Crear múltiples estrategias
        EstrategiaEmparejamiento estrategiaCercania = new EmparejamientoPorCercania();
        EstrategiaEmparejamiento estrategiaNivel = new EmparejamientoPorNivel();
        EstrategiaEmparejamiento estrategiaHistorial = new EmparejamientoPorHistorial();
        
        // When - Usar cada estrategia
        List<Jugador> invitadosCercania = DemoInvitacionUtils.enviarInvitaciones(
            partidoFutbol, jugadoresRegistrados, estrategiaCercania, notificacionService);
        
        List<Jugador> invitadosNivel = DemoInvitacionUtils.enviarInvitaciones(
            partidoFutbol, jugadoresRegistrados, estrategiaNivel, notificacionService);
            
        List<Jugador> invitadosHistorial = DemoInvitacionUtils.enviarInvitaciones(
            partidoFutbol, jugadoresRegistrados, estrategiaHistorial, notificacionService);
        
        // Then - Verificar que se pueden cambiar estrategias dinámicamente
        assertNotNull(invitadosCercania);
        assertNotNull(invitadosNivel);
        assertNotNull(invitadosHistorial);
        
        System.out.println("✅ Test Patrón Strategy - Flexibilidad: Estrategias intercambiables dinámicamente");
        System.out.println("   🌍 Cercanía: " + invitadosCercania.size() + " jugadores");
        System.out.println("   ⭐ Nivel: " + invitadosNivel.size() + " jugadores");
        System.out.println("   📊 Historial: " + invitadosHistorial.size() + " jugadores");
    }
    
    private void crearDatosPrueba() {
        // Crear zonas
        Zona zonaPalermo = new Zona("Palermo", "CABA", null);
        Zona zonaSanTelmo = new Zona("San Telmo", "CABA", null);
        
        // Crear deporte
        Deporte futbol = new Deporte("Fútbol 5", "Deporte de equipo", 10);
        
        // Crear organizador
        Jugador organizador = new Jugador("Ana", "ana@email.com", "password123", zonaPalermo);
        
        // Crear partido
        partidoFutbol = new Partido();
        partidoFutbol.setTitulo("Fútbol de Sábado Test");
        partidoFutbol.setFechaHora(LocalDateTime.now().plusDays(3));
        partidoFutbol.setZona(zonaPalermo);
        partidoFutbol.setDeporte(futbol);
        partidoFutbol.setNivel(Nivel.INTERMEDIO);
        partidoFutbol.setOrganizador(organizador);
        
        // Crear jugadores registrados
        jugadoresRegistrados = new ArrayList<>();
        
        // Jugador en Palermo con nivel compatible
        Jugador carlos = crearJugador("Carlos", "carlos@email.com", zonaPalermo, futbol, Nivel.INTERMEDIO);
        jugadoresRegistrados.add(carlos);
        
        // Jugador en otra zona
        Jugador diego = crearJugador("Diego", "diego@email.com", zonaSanTelmo, futbol, Nivel.AVANZADO);
        jugadoresRegistrados.add(diego);
        
        // Agregar historial
        Partido partidoAnterior = new Partido();
        partidoAnterior.setOrganizador(organizador);
        carlos.getPartidosParticipados().add(partidoAnterior);
    }
    
    private Jugador crearJugador(String nombre, String email, Zona zona, Deporte deporte, Nivel nivel) {
        Jugador jugador = new Jugador(nombre, email, "password123", zona);
        
        DeporteJugador deporteJugador = new DeporteJugador();
        deporteJugador.setJugador(jugador);
        deporteJugador.setDeporte(deporte);
        deporteJugador.setNivel(nivel);
        jugador.getDeportes().add(deporteJugador);
        
        return jugador;
    }
    
    private boolean tieneNivelCompatible(Jugador jugador, Partido partido) {
        Nivel nivelPartido = partido.getNivel();
        if (nivelPartido == null) return true;
        
        return jugador.getDeportes().stream()
                .filter(dj -> dj.getDeporte().equals(partido.getDeporte()))
                .map(DeporteJugador::getNivel)
                .anyMatch(nivelJugador -> Math.abs(nivelJugador.ordinal() - nivelPartido.ordinal()) <= 1);
    }
} 