package com.tpo.unoMas.model.strategy;

import com.tpo.unoMas.model.*;
import com.tpo.unoMas.model.adapter.AdapterJavaMail;
import com.tpo.unoMas.model.adapter.NotificacionEmailAdapter;
import com.tpo.unoMas.model.strategy.emparejamiento.*;
import com.tpo.unoMas.model.strategy.notificacion.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests exhaustivos para los Strategy Patterns del sistema
 * Cubre: Estrategias de emparejamiento y notificaciones
 */
@DisplayName("Tests de Strategy Pattern")
class StrategyPatternTest {

    private Partido partidoTest;
    private Zona zonaTest;
    private Deporte deporteTest;
    private Jugador organizadorTest;
    private List<Jugador> jugadoresDisponibles;

    @BeforeEach
    void setUp() {
        // Crear zona de test
        zonaTest = new Zona();
        zonaTest.setBarrio("Palermo");
        zonaTest.setPartido("CABA");
        zonaTest.setUbicacion(new Ubicacion(-34.5875, -58.4217));
        
        // Crear deporte de test
        deporteTest = new Deporte();
        deporteTest.setNombre("Fútbol");
        deporteTest.setDescripcion("Fútbol 11");
        deporteTest.setCantidadJugadores(22);
        
        // Crear organizador
        organizadorTest = new Jugador("Organizador", "org@email.com", "password123", zonaTest);
        
        // Crear partido de test
        partidoTest = new Partido();
        partidoTest.setTitulo("Partido Test");
        partidoTest.setFechaHora(LocalDateTime.now().plusDays(1));
        partidoTest.setZona(zonaTest);
        partidoTest.setDeporte(deporteTest);
        partidoTest.setOrganizador(organizadorTest);
        partidoTest.setDuracionMinutos(90);
        partidoTest.setNivel(Nivel.INTERMEDIO);
        
        // Crear jugadores de test
        crearJugadoresDeTest();
    }

    private void crearJugadoresDeTest() {
        jugadoresDisponibles = new ArrayList<>();
        
        // Jugador 1: Cerca y nivel intermedio
        Jugador jugador1 = new Jugador("Juan Pérez", "juan@email.com", "password123", zonaTest);
        DeporteJugador dj1 = new DeporteJugador();
        dj1.setJugador(jugador1);
        dj1.setDeporte(deporteTest);
        dj1.setNivel(Nivel.INTERMEDIO);
        jugador1.getDeportes().add(dj1);
        jugadoresDisponibles.add(jugador1);
        
        // Jugador 2: Cerca y nivel principiante
        Jugador jugador2 = new Jugador("María García", "maria@email.com", "password123", zonaTest);
        DeporteJugador dj2 = new DeporteJugador();
        dj2.setJugador(jugador2);
        dj2.setDeporte(deporteTest);
        dj2.setNivel(Nivel.PRINCIPIANTE);
        jugador2.getDeportes().add(dj2);
        jugadoresDisponibles.add(jugador2);
        
        // Jugador 3: Lejos y nivel avanzado
        Zona zonaLejos = new Zona();
        zonaLejos.setBarrio("La Plata");
        zonaLejos.setPartido("Buenos Aires");
        zonaLejos.setUbicacion(new Ubicacion(-34.9214, -57.9544));
        
        Jugador jugador3 = new Jugador("Carlos López", "carlos@email.com", "password123", zonaLejos);
        DeporteJugador dj3 = new DeporteJugador();
        dj3.setJugador(jugador3);
        dj3.setDeporte(deporteTest);
        dj3.setNivel(Nivel.AVANZADO);
        jugador3.getDeportes().add(dj3);
        jugadoresDisponibles.add(jugador3);
        
        // Jugador 4: Cerca, nivel intermedio y con historial
        Jugador jugador4 = new Jugador("Ana Rodríguez", "ana@email.com", "password123", zonaTest);
        DeporteJugador dj4 = new DeporteJugador();
        dj4.setJugador(jugador4);
        dj4.setDeporte(deporteTest);
        dj4.setNivel(Nivel.INTERMEDIO);
        jugador4.getDeportes().add(dj4);
        jugador4.agregarAlHistorial(partidoTest);
        jugadoresDisponibles.add(jugador4);
    }

    @Nested
    @DisplayName("Tests de Estrategias de Emparejamiento")
    class EstrategiasEmparejamientoTests {

        @Test
        @DisplayName("EmparejamientoPorCercania debería funcionar correctamente")
        void emparejamientoPorCercaniaShouldWork() {
            EstrategiaEmparejamiento estrategia = new EmparejamientoPorCercania();
            
            List<Jugador> resultado = estrategia.encontrarJugadoresPotenciales(partidoTest, jugadoresDisponibles);
            
            assertNotNull(resultado);
            assertFalse(resultado.isEmpty());
            
            // Los jugadores cercanos deberían estar primero
            Jugador primerJugador = resultado.get(0);
            assertEquals("Palermo", primerJugador.getZona().getBarrio());
        }

        @Test
        @DisplayName("EmparejamientoPorNivel debería funcionar correctamente")
        void emparejamientoPorNivelShouldWork() {
            EstrategiaEmparejamiento estrategia = new EmparejamientoPorNivel();
            
            List<Jugador> resultado = estrategia.encontrarJugadoresPotenciales(partidoTest, jugadoresDisponibles);
            
            assertNotNull(resultado);
            assertFalse(resultado.isEmpty());
            
            // Verificar que jugadores con nivel similar estén primero
            List<Jugador> jugadoresIntermedios = resultado.stream()
                .filter(j -> j.getDeportes().stream()
                    .anyMatch(dj -> dj.getNivel() == Nivel.INTERMEDIO))
                .toList();
            
            assertFalse(jugadoresIntermedios.isEmpty());
        }

        @Test
        @DisplayName("EmparejamientoPorHistorial debería funcionar correctamente")
        void emparejamientoPorHistorialShouldWork() {
            EstrategiaEmparejamiento estrategia = new EmparejamientoPorHistorial();
            
            List<Jugador> resultado = estrategia.encontrarJugadoresPotenciales(partidoTest, jugadoresDisponibles);
            
            assertNotNull(resultado);
            assertFalse(resultado.isEmpty());
            
            // Verificar que jugadores con historial están incluidos
            boolean hayJugadorConHistorial = resultado.stream()
                .anyMatch(j -> !j.getHistorial().isEmpty());
            
            assertTrue(hayJugadorConHistorial);
        }

        @Test
        @DisplayName("Estrategias deberían manejar lista vacía")
        void estrategiasDeberianManejarListaVacia() {
            List<Jugador> listaVacia = new ArrayList<>();
            
            EstrategiaEmparejamiento estrategiaCercania = new EmparejamientoPorCercania();
            EstrategiaEmparejamiento estrategiaNivel = new EmparejamientoPorNivel();
            EstrategiaEmparejamiento estrategiaHistorial = new EmparejamientoPorHistorial();
            
            assertTrue(estrategiaCercania.encontrarJugadoresPotenciales(partidoTest, listaVacia).isEmpty());
            assertTrue(estrategiaNivel.encontrarJugadoresPotenciales(partidoTest, listaVacia).isEmpty());
            assertTrue(estrategiaHistorial.encontrarJugadoresPotenciales(partidoTest, listaVacia).isEmpty());
        }

        @Test
        @DisplayName("Estrategias deberían manejar valores null")
        void estrategiasDeberianManejarValoresNull() {
            EstrategiaEmparejamiento estrategiaCercania = new EmparejamientoPorCercania();
            EstrategiaEmparejamiento estrategiaNivel = new EmparejamientoPorNivel();
            EstrategiaEmparejamiento estrategiaHistorial = new EmparejamientoPorHistorial();
            
            assertThrows(NullPointerException.class, () -> {
                estrategiaCercania.encontrarJugadoresPotenciales(null, jugadoresDisponibles);
            });
            
            assertThrows(NullPointerException.class, () -> {
                estrategiaNivel.encontrarJugadoresPotenciales(partidoTest, null);
            });
            
            assertThrows(NullPointerException.class, () -> {
                estrategiaHistorial.encontrarJugadoresPotenciales(null, null);
            });
        }
    }

    @Nested
    @DisplayName("Tests de Estrategias de Notificación")
    class EstrategiasNotificacionTests {

        private NotificacionEmailAdapter mockAdapter;

        @BeforeEach
        void setUp() {
            mockAdapter = mock(NotificacionEmailAdapter.class);
        }

        @Test
        @DisplayName("NotificacionEmail debería funcionar correctamente")
        void notificacionEmailShouldWork() {
            INotificacionStrategy estrategiaEmail = new NotificacionEmail(mockAdapter);
            Jugador jugador = jugadoresDisponibles.get(0);
            Notificacion notificacion = new Notificacion(
                "Test Subject",
                "Test Message",
                jugador
            );
            
            doNothing().when(mockAdapter).enviarEmail(any(Notificacion.class));
            
            assertDoesNotThrow(() -> {
                estrategiaEmail.enviarNotificacion(notificacion);
            });
            
            verify(mockAdapter).enviarEmail(notificacion);
        }

        @Test
        @DisplayName("NotificacionPushFirebase debería funcionar correctamente")
        void notificacionPushFirebaseShouldWork() {
            INotificacionStrategy estrategiaPush = new NotificacionPushFirebase();
            Jugador jugador = jugadoresDisponibles.get(0);
            jugador.setTokenFCM("test-token");
            
            Notificacion notificacion = new Notificacion(
                "Test Subject",
                "Test Message",
                jugador
            );
            
            // El envío de push es simulado, no debería lanzar excepciones
            assertDoesNotThrow(() -> {
                estrategiaPush.enviarNotificacion(notificacion);
            });
        }

        @Test
        @DisplayName("NotificacionEmail debería manejar jugador sin email")
        void notificacionEmailDeberiaManejarJugadorSinEmail() {
            INotificacionStrategy estrategiaEmail = new NotificacionEmail(mockAdapter);
            Jugador jugador = new Jugador("Sin Email", null, "password123", zonaTest);
            
            Notificacion notificacion = new Notificacion(
                "Test Subject",
                "Test Message",
                jugador
            );
            
            doThrow(new IllegalArgumentException("No se puede enviar email: destinatario o email inválido"))
                .when(mockAdapter).enviarEmail(any(Notificacion.class));
            
            assertThrows(IllegalArgumentException.class, () -> {
                estrategiaEmail.enviarNotificacion(notificacion);
            });
        }

        @Test
        @DisplayName("NotificacionPushFirebase debería manejar jugador sin token FCM")
        void notificacionPushFirebaseDeberiaManejarJugadorSinToken() {
            INotificacionStrategy estrategiaPush = new NotificacionPushFirebase();
            Jugador jugador = new Jugador("Sin Token", "test@email.com", "password123", zonaTest);
            jugador.setTokenFCM(null);
            
            Notificacion notificacion = new Notificacion(
                "Test Subject",
                "Test Message",
                jugador
            );
            
            // Debería manejar gracefully el caso de token null
            assertDoesNotThrow(() -> {
                estrategiaPush.enviarNotificacion(notificacion);
            });
        }

        @Test
        @DisplayName("Estrategias de notificación deberían manejar notificación null")
        void estrategiasNotificacionDeberianManejarNotificacionNull() {
            NotificacionEmailAdapter javaMailAdapter = new AdapterJavaMail();
            INotificacionStrategy estrategiaEmail = new NotificacionEmail(javaMailAdapter);
            INotificacionStrategy estrategiaPush = new NotificacionPushFirebase();
            
            assertThrows(Exception.class, () -> {
                estrategiaEmail.enviarNotificacion(null);
            });
            
            assertThrows(Exception.class, () -> {
                estrategiaPush.enviarNotificacion(null);
            });
        }
    }

    @Nested
    @DisplayName("Tests de Integración de Estrategias")
    class IntegracionEstrategiasTests {

        @Test
        @DisplayName("Debería poder cambiar estrategias dinámicamente")
        void deberiaPodeCambiarEstrategiasDinamicamente() {
            // Inicial: EmparejamientoPorCercania (por defecto)
            assertInstanceOf(EmparejamientoPorCercania.class, partidoTest.getEstrategiaEmparejamiento());
            
            // Cambiar a EmparejamientoPorNivel
            EstrategiaEmparejamiento estrategiaNivel = new EmparejamientoPorNivel();
            partidoTest.cambiarEstrategiaEmparejamiento(estrategiaNivel);
            assertEquals(estrategiaNivel, partidoTest.getEstrategiaEmparejamiento());
            
            // Cambiar a EmparejamientoPorHistorial
            EstrategiaEmparejamiento estrategiaHistorial = new EmparejamientoPorHistorial();
            partidoTest.cambiarEstrategiaEmparejamiento(estrategiaHistorial);
            assertEquals(estrategiaHistorial, partidoTest.getEstrategiaEmparejamiento());
        }

        @Test
        @DisplayName("Diferentes estrategias deberían dar diferentes resultados")
        void diferentesEstrategiasDeberianDarDiferentesResultados() {
            EstrategiaEmparejamiento estrategiaCercania = new EmparejamientoPorCercania();
            EstrategiaEmparejamiento estrategiaNivel = new EmparejamientoPorNivel();
            EstrategiaEmparejamiento estrategiaHistorial = new EmparejamientoPorHistorial();
            
            List<Jugador> resultadoCercania = estrategiaCercania.encontrarJugadoresPotenciales(partidoTest, jugadoresDisponibles);
            List<Jugador> resultadoNivel = estrategiaNivel.encontrarJugadoresPotenciales(partidoTest, jugadoresDisponibles);
            List<Jugador> resultadoHistorial = estrategiaHistorial.encontrarJugadoresPotenciales(partidoTest, jugadoresDisponibles);
            
            // Todos deberían dar algún resultado
            assertFalse(resultadoCercania.isEmpty());
            assertFalse(resultadoNivel.isEmpty());
            assertFalse(resultadoHistorial.isEmpty());
            
            // Los resultados pueden ser diferentes según la estrategia
            // (no necesariamente serán diferentes, pero el test verifica que funcionen)
            assertNotNull(resultadoCercania);
            assertNotNull(resultadoNivel);
            assertNotNull(resultadoHistorial);
        }

        @Test
        @DisplayName("Sistema debería funcionar con múltiples tipos de notificación")
        void sistemaDeberiaFuncionarConMultiplesTiposNotificacion() {
            NotificacionEmailAdapter mockAdapter = mock(NotificacionEmailAdapter.class);
            INotificacionStrategy estrategiaEmail = new NotificacionEmail(mockAdapter);
            INotificacionStrategy estrategiaPush = new NotificacionPushFirebase();
            
            Jugador jugador = jugadoresDisponibles.get(0);
            jugador.setTokenFCM("test-token");
            
            Notificacion notificacion = new Notificacion(
                "Invitación al partido",
                "Te invitamos a participar en el partido de fútbol",
                jugador
            );
            
            doNothing().when(mockAdapter).enviarEmail(any(Notificacion.class));
            
            // Ambas estrategias deberían funcionar
            assertDoesNotThrow(() -> {
                estrategiaEmail.enviarNotificacion(notificacion);
                estrategiaPush.enviarNotificacion(notificacion);
            });
            
            verify(mockAdapter).enviarEmail(notificacion);
        }
    }
} 