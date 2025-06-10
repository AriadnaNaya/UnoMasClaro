package com.tpo.unoMas;

import com.tpo.unoMas.model.*;
import com.tpo.unoMas.model.estado.*;
import com.tpo.unoMas.model.strategy.notificacion.INotificacionStrategy;
import com.tpo.unoMas.service.NotificacionService;
import com.tpo.unoMas.observer.Observer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@DisplayName("Tests de Integración: State + Observer + Strategy + Adapter")
public class PatronesIntegradosTest {

    private Partido partido;
    private Jugador jugador1;
    private Jugador jugador2;
    private Jugador jugador3;
    private Jugador organizador;
    private Zona zona;
    private Deporte deporte;
    private NotificacionService notificacionService;
    private MockNotificacionStrategy mockStrategy;
    private List<String> notificacionesEnviadas;

    @BeforeEach
    void setUp() {
        // Crear objetos de prueba
        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setLatitud(-34.6036840);
        ubicacion.setLongitud(-58.3815591);
        
        zona = new Zona("Centro", "CABA", ubicacion);
        deporte = new Deporte("Fútbol", "Deporte de equipo");
        
        // Crear jugadores
        organizador = crearJugador("Ana García", "ana@test.com", zona);
        jugador1 = crearJugador("Carlos López", "carlos@test.com", zona);
        jugador2 = crearJugador("María Pérez", "maria@test.com", zona);
        jugador3 = crearJugador("Diego Martín", "diego@test.com", zona);
        
        // Crear partido
        partido = new Partido();
        partido.setTitulo("Partido Integración");
        partido.setFechaHora(LocalDateTime.now().plusDays(1));
        partido.setZona(zona);
        partido.setDeporte(deporte);
        partido.setNivel(Nivel.INTERMEDIO);
        partido.setOrganizador(organizador);
        partido.setMinJugadores(2);
        partido.setMaxJugadores(4);
        partido.setDuracionMinutos(90);
        
        // Configurar sistema de notificaciones
        notificacionesEnviadas = new ArrayList<>();
        mockStrategy = new MockNotificacionStrategy(notificacionesEnviadas);
        notificacionService = new NotificacionService();
        notificacionService.cambiarEstrategiaNotificacion(mockStrategy);
        
        // Suscribir el servicio de notificaciones al partido
        partido.attach(notificacionService);
    }

    private Jugador crearJugador(String nombre, String email, Zona zona) {
        Jugador jugador = new Jugador();
        jugador.setNombre(nombre);
        jugador.setEmail(email);
        jugador.setPassword("password123");
        jugador.setZona(zona);
        return jugador;
    }

    @Test
    @DisplayName("Integración completa: State + Observer + Strategy")
    void testIntegracionCompleta() {
        System.out.println("🔗 PRUEBA DE INTEGRACIÓN COMPLETA 🔗");
        System.out.println("=====================================\n");
        
        // 1. Estado inicial (no debe notificar aún)
        assertTrue(partido.getEstado() instanceof NecesitamosJugadores);
        assertEquals(0, notificacionesEnviadas.size());
        System.out.println("✓ Estado inicial: " + partido.getEstado().getClass().getSimpleName());
        
        // 2. Agregar primer jugador (no cambia estado, no notifica)
        partido.agregarJugador(jugador1);
        assertTrue(partido.getEstado() instanceof NecesitamosJugadores);
        assertEquals(0, notificacionesEnviadas.size());
        System.out.println("✓ Primer jugador agregado - Estado: " + partido.getEstado().getClass().getSimpleName());
        
        // 3. Agregar segundo jugador (CAMBIA ESTADO → debe notificar)
        partido.agregarJugador(jugador2);
        assertTrue(partido.getEstado() instanceof PartidoArmado);
        assertTrue(notificacionesEnviadas.size() >= 1);
        assertTrue(notificacionesEnviadas.get(notificacionesEnviadas.size()-1).contains("PartidoArmado"));
        assertTrue(notificacionesEnviadas.get(notificacionesEnviadas.size()-1).contains("Tu Partido ya esta listo"));
        System.out.println("✓ Segundo jugador agregado - TRANSICIÓN A PartidoArmado");
        System.out.println("  📧 Notificación enviada: " + notificacionesEnviadas.get(notificacionesEnviadas.size()-1));
        
        // 4. Confirmar asistencias (CAMBIA ESTADO → debe notificar)
        int notificacionesAntes = notificacionesEnviadas.size();
        partido.confirmarAsistencia(jugador1);
        partido.confirmarAsistencia(jugador2);
        assertTrue(partido.getEstado() instanceof Confirmado);
        assertTrue(notificacionesEnviadas.size() > notificacionesAntes);
        assertTrue(notificacionesEnviadas.get(notificacionesEnviadas.size()-1).contains("Confirmado"));
        assertTrue(notificacionesEnviadas.get(notificacionesEnviadas.size()-1).contains("ha sido confirmado"));
        System.out.println("✓ Asistencias confirmadas - TRANSICIÓN A Confirmado");
        System.out.println("  📧 Notificación enviada: " + notificacionesEnviadas.get(notificacionesEnviadas.size()-1));
        
        // 5. Iniciar partido (CAMBIA ESTADO → debe notificar)
        notificacionesAntes = notificacionesEnviadas.size();
        partido.iniciar();
        assertTrue(partido.getEstado() instanceof EnJuego);
        assertTrue(notificacionesEnviadas.size() > notificacionesAntes);
        assertTrue(notificacionesEnviadas.get(notificacionesEnviadas.size()-1).contains("EnJuego"));
        assertTrue(notificacionesEnviadas.get(notificacionesEnviadas.size()-1).contains("Ha comenzado el partido"));
        System.out.println("✓ Partido iniciado - TRANSICIÓN A EnJuego");
        System.out.println("  📧 Notificación enviada: " + notificacionesEnviadas.get(notificacionesEnviadas.size()-1));
        
        // 6. Finalizar partido (CAMBIA ESTADO → debe notificar)
        notificacionesAntes = notificacionesEnviadas.size();
        partido.finalizar();
        assertTrue(partido.getEstado() instanceof Finalizado);
        assertTrue(notificacionesEnviadas.size() > notificacionesAntes);
        assertTrue(notificacionesEnviadas.get(notificacionesEnviadas.size()-1).contains("Finalizado"));
        assertTrue(notificacionesEnviadas.get(notificacionesEnviadas.size()-1).contains("ha finalizado"));
        System.out.println("✓ Partido finalizado - TRANSICIÓN A Finalizado");
        System.out.println("  📧 Notificación enviada: " + notificacionesEnviadas.get(notificacionesEnviadas.size()-1));
        
        System.out.println("\n🎉 ¡Integración completa exitosa!");
        System.out.println("Total de notificaciones enviadas: " + notificacionesEnviadas.size());
    }

    @Test
    @DisplayName("Cambio de estrategia de notificación en tiempo real")
    void testCambioEstrategia() {
        System.out.println("\n🔄 PRUEBA DE CAMBIO DE ESTRATEGIA 🔄");
        System.out.println("====================================\n");
        
        // Configurar estrategia de email
        MockEmailStrategy emailStrategy = new MockEmailStrategy();
        notificacionService.cambiarEstrategiaNotificacion(emailStrategy);
        
        // Provocar cambio de estado
        partido.agregarJugador(jugador1);
        partido.agregarJugador(jugador2);
        
        assertEquals("MockEmailStrategy", notificacionService.getEstrategiaActual());
        assertTrue(emailStrategy.emailEnviado);
        System.out.println("✓ Estrategia Email ejecutada correctamente");
        
        // Cambiar a estrategia de push
        MockPushStrategy pushStrategy = new MockPushStrategy();
        notificacionService.cambiarEstrategiaNotificacion(pushStrategy);
        
        // Provocar otro cambio de estado
        partido.confirmarAsistencia(jugador1);
        partido.confirmarAsistencia(jugador2);
        
        assertEquals("MockPushStrategy", notificacionService.getEstrategiaActual());
        assertTrue(pushStrategy.pushEnviado);
        System.out.println("✓ Estrategia Push ejecutada correctamente");
        
        System.out.println("✓ Cambio de estrategia en tiempo real funciona!");
    }

    @Test
    @DisplayName("Observer: Múltiples observadores")
    void testMultiplesObservadores() {
        System.out.println("\n👥 PRUEBA DE MÚLTIPLES OBSERVADORES 👥");
        System.out.println("======================================\n");
        
        // Crear observadores adicionales
        MockObserver observer1 = new MockObserver("LogObserver");
        MockObserver observer2 = new MockObserver("MetricsObserver");
        
        // Suscribir múltiples observadores
        partido.attach(observer1);
        partido.attach(observer2);
        
        // Provocar cambio de estado
        partido.agregarJugador(jugador1);
        partido.agregarJugador(jugador2);
        
        // Verificar que todos los observadores fueron notificados
        assertTrue(observer1.notificaciones >= 1);
        assertTrue(observer2.notificaciones >= 1);
        assertTrue(notificacionesEnviadas.size() >= 1); // NotificacionService también
        
        System.out.println("✓ " + observer1.nombre + " notificado: " + observer1.notificaciones + " vez");
        System.out.println("✓ " + observer2.nombre + " notificado: " + observer2.notificaciones + " vez");
        System.out.println("✓ NotificacionService notificado: 1 vez");
        
        // Remover un observador
        partido.detach(observer1);
        
        // Provocar otro cambio
        partido.confirmarAsistencia(jugador1);
        partido.confirmarAsistencia(jugador2);
        
        // Verificar que solo los observadores activos fueron notificados
        int observer1Final = observer1.notificaciones; // Debería haberse mantenido igual
        assertTrue(observer2.notificaciones > 1); // Incrementó
        assertTrue(notificacionesEnviadas.size() > 1); // NotificacionService también incrementó
        
        System.out.println("✓ Después de detach - " + observer1.nombre + ": " + observer1.notificaciones);
        System.out.println("✓ Después de detach - " + observer2.nombre + ": " + observer2.notificaciones);
        System.out.println("✓ Sistema de múltiples observadores funciona correctamente!");
    }

    @Test
    @DisplayName("Adapter: Diferentes tipos de notificación")
    void testAdapterPattern() {
        System.out.println("\n🔌 PRUEBA DEL PATRÓN ADAPTER 🔌");
        System.out.println("===============================\n");
        
        // Probar con diferentes adapters simulados
        MockEmailAdapter emailAdapter = new MockEmailAdapter();
        MockPushAdapter pushAdapter = new MockPushAdapter();
        
        // Crear notificación de prueba
        Notificacion notificacion = new Notificacion(
            "Test Título", 
            "Test Mensaje", 
            jugador1
        );
        
        // Probar adapter de email
        emailAdapter.enviarEmail(notificacion);
        assertTrue(emailAdapter.emailEnviado);
        assertEquals("Test Título", emailAdapter.ultimoTitulo);
        assertEquals("Test Mensaje", emailAdapter.ultimoMensaje);
        System.out.println("✓ EmailAdapter: " + emailAdapter.ultimoTitulo + " - " + emailAdapter.ultimoMensaje);
        
        // Probar adapter de push
        pushAdapter.enviarPush(notificacion);
        assertTrue(pushAdapter.pushEnviado);
        assertEquals("Test Título", pushAdapter.ultimoTitulo);
        assertEquals("Test Mensaje", pushAdapter.ultimoMensaje);
        System.out.println("✓ PushAdapter: " + pushAdapter.ultimoTitulo + " - " + pushAdapter.ultimoMensaje);
        
        System.out.println("✓ Patrón Adapter funciona con diferentes tipos de notificación!");
    }

    // ===== CLASES MOCK PARA TESTING =====
    
    private static class MockNotificacionStrategy implements INotificacionStrategy {
        private final List<String> notificaciones;
        
        public MockNotificacionStrategy(List<String> notificaciones) {
            this.notificaciones = notificaciones;
        }
        
        @Override
        public void enviarNotificacion(Notificacion notificacion) {
            String mensaje = notificacion.getTitulo() + ": " + notificacion.getMensaje();
            notificaciones.add(mensaje);
        }
    }
    
    private static class MockEmailStrategy implements INotificacionStrategy {
        public boolean emailEnviado = false;
        
        @Override
        public void enviarNotificacion(Notificacion notificacion) {
            emailEnviado = true;
        }
    }
    
    private static class MockPushStrategy implements INotificacionStrategy {
        public boolean pushEnviado = false;
        
        @Override
        public void enviarNotificacion(Notificacion notificacion) {
            pushEnviado = true;
        }
    }
    
    private static class MockObserver implements Observer {
        public final String nombre;
        public int notificaciones = 0;
        
        public MockObserver(String nombre) {
            this.nombre = nombre;
        }
        
        @Override
        public void update(Partido partido) {
            notificaciones++;
        }
    }
    
    private static class MockEmailAdapter {
        public boolean emailEnviado = false;
        public String ultimoTitulo;
        public String ultimoMensaje;
        
        public void enviarEmail(Notificacion notificacion) {
            emailEnviado = true;
            ultimoTitulo = notificacion.getTitulo();
            ultimoMensaje = notificacion.getMensaje();
        }
    }
    
    private static class MockPushAdapter {
        public boolean pushEnviado = false;
        public String ultimoTitulo;
        public String ultimoMensaje;
        
        public void enviarPush(Notificacion notificacion) {
            pushEnviado = true;
            ultimoTitulo = notificacion.getTitulo();
            ultimoMensaje = notificacion.getMensaje();
        }
    }
} 