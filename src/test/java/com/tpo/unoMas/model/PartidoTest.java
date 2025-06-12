package com.tpo.unoMas.model;

import com.tpo.unoMas.model.estado.*;
import com.tpo.unoMas.model.strategy.emparejamiento.*;
import com.tpo.unoMas.model.observer.Observer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests exhaustivos para la entidad Partido
 * Cubre: State Pattern, Strategy Pattern, Observer Pattern, validaciones, gestión de jugadores
 */
@DisplayName("Tests de Partido")
class PartidoTest {

    private Partido partidoTest;
    private Zona zonaTest;
    private Deporte deporteTest;
    private Jugador organizadorTest;
    private Jugador jugadorTest1;
    private Jugador jugadorTest2;

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
        
        // Crear jugadores de test
        jugadorTest1 = new Jugador("Juan Pérez", "juan@email.com", "password123", zonaTest);
        jugadorTest2 = new Jugador("María García", "maria@email.com", "password123", zonaTest);
        
        // Crear partido de test
        partidoTest = new Partido();
        partidoTest.setTitulo("Partido Test");
        partidoTest.setFechaHora(LocalDateTime.now().plusDays(1));
        partidoTest.setZona(zonaTest);
        partidoTest.setDeporte(deporteTest);
        partidoTest.setOrganizador(organizadorTest);
        partidoTest.setDuracionMinutos(90);
        partidoTest.setNivel(Nivel.INTERMEDIO);
    }

    @Nested
    @DisplayName("Tests de State Pattern")
    class StatePatternTests {

        @Test
        @DisplayName("Debería inicializar con estado NecesitamosJugadores")
        void deberiaInicializarConEstadoNecesitamosJugadores() {
            Partido partido = new Partido();
            assertInstanceOf(NecesitamosJugadores.class, partido.getEstado());
        }

        @Test
        @DisplayName("Debería cambiar estado correctamente")
        void deberiaCambiarEstadoCorrectamente() {
            // Estado inicial
            assertInstanceOf(NecesitamosJugadores.class, partidoTest.getEstado());
            
            // Cambiar a PartidoArmado
            partidoTest.cambiarEstado(new PartidoArmado());
            assertInstanceOf(PartidoArmado.class, partidoTest.getEstado());
            
            // Cambiar a Confirmado
            partidoTest.cambiarEstado(new Confirmado());
            assertInstanceOf(Confirmado.class, partidoTest.getEstado());
            
            // Cambiar a EnJuego
            partidoTest.cambiarEstado(new EnJuego());
            assertInstanceOf(EnJuego.class, partidoTest.getEstado());
            
            // Cambiar a Finalizado
            partidoTest.cambiarEstado(new Finalizado());
            assertInstanceOf(Finalizado.class, partidoTest.getEstado());
        }

        @Test
        @DisplayName("Debería cancelar partido desde cualquier estado válido")
        void deberiaCancelarPartidoDesdeEstadoValido() {
            // Desde NecesitamosJugadores
            partidoTest.cancelar();
            assertInstanceOf(Cancelado.class, partidoTest.getEstado());
            
            // Crear nuevo partido para probar desde PartidoArmado
            Partido partido2 = new Partido();
            partido2.setTitulo("Partido Test 2");
            partido2.setFechaHora(LocalDateTime.now().plusDays(1));
            partido2.setZona(zonaTest);
            partido2.setDeporte(deporteTest);
            partido2.setOrganizador(organizadorTest);
            partido2.setDuracionMinutos(90);
            
            partido2.cambiarEstado(new PartidoArmado());
            partido2.cancelar();
            assertInstanceOf(Cancelado.class, partido2.getEstado());
        }

        @Test
        @DisplayName("No debería cancelar partido finalizado")
        void noDeberiaCancelarPartidoFinalizado() {
            partidoTest.cambiarEstado(new Finalizado());
            
            assertThrows(IllegalStateException.class, () -> {
                partidoTest.cancelar();
            });
        }

        @Test
        @DisplayName("Debería agregar jugador en estado válido")
        void deberiaAgregarJugadorEnEstadoValido() {
            // En NecesitamosJugadores debería poder agregar
            assertDoesNotThrow(() -> {
                partidoTest.agregarJugador(jugadorTest1);
            });
            
            assertTrue(partidoTest.getJugadores().contains(jugadorTest1));
        }

        @Test
        @DisplayName("No debería agregar jugador en estado Finalizado")
        void noDeberiaAgregarJugadorEnEstadoFinalizado() {
            partidoTest.cambiarEstado(new Finalizado());
            
            assertThrows(IllegalStateException.class, () -> {
                partidoTest.agregarJugador(jugadorTest1);
            });
        }

        @Test
        @DisplayName("Debería iniciar partido desde estado Confirmado")
        void deberiaIniciarPartidoDesdeEstadoConfirmado() {
            partidoTest.cambiarEstado(new Confirmado());
            
            assertDoesNotThrow(() -> {
                partidoTest.iniciar();
            });
            
            assertInstanceOf(EnJuego.class, partidoTest.getEstado());
        }

        @Test
        @DisplayName("Debería finalizar partido desde estado EnJuego")
        void deberiaFinalizarPartidoDesdeEstadoEnJuego() {
            partidoTest.cambiarEstado(new EnJuego());
            
            assertDoesNotThrow(() -> {
                partidoTest.finalizar();
            });
            
            assertInstanceOf(Finalizado.class, partidoTest.getEstado());
        }
    }

    @Nested
    @DisplayName("Tests de Strategy Pattern - Emparejamiento")
    class StrategyPatternEmparejamientoTests {

        @Test
        @DisplayName("Debería inicializar con estrategia EmparejamientoPorCercania por defecto")
        void deberiaInicializarConEstrategiaDefecto() {
            Partido partido = new Partido();
            assertInstanceOf(EmparejamientoPorCercania.class, partido.getEstrategiaEmparejamiento());
        }

        @Test
        @DisplayName("Debería cambiar estrategia de emparejamiento")
        void deberiaCambiarEstrategiaEmparejamiento() {
            EstrategiaEmparejamiento nuevaEstrategia = new EmparejamientoPorNivel();
            
            partidoTest.cambiarEstrategiaEmparejamiento(nuevaEstrategia);
            
            assertEquals(nuevaEstrategia, partidoTest.getEstrategiaEmparejamiento());
        }

        @Test
        @DisplayName("Debería encontrar jugadores usando estrategia por cercanía")
        void deberiaEncontrarJugadoresUsandoEstrategiaCercania() {
            // Crear jugadores con diferentes ubicaciones
            Zona zonaCerca = new Zona();
            zonaCerca.setBarrio("Belgrano");
            zonaCerca.setPartido("CABA");
            zonaCerca.setUbicacion(new Ubicacion(-34.5631, -58.4550)); // Cerca de Palermo
            
            Zona zonaLejos = new Zona();
            zonaLejos.setBarrio("La Plata");
            zonaLejos.setPartido("Buenos Aires");
            zonaLejos.setUbicacion(new Ubicacion(-34.9214, -57.9544)); // Lejos de Palermo
            
            Jugador jugadorCerca = new Jugador("Cerca", "cerca@email.com", "pass123", zonaCerca);
            Jugador jugadorLejos = new Jugador("Lejos", "lejos@email.com", "pass123", zonaLejos);
            
            List<Jugador> jugadoresDisponibles = Arrays.asList(jugadorCerca, jugadorLejos);
            
            partidoTest.cambiarEstrategiaEmparejamiento(new EmparejamientoPorCercania());
            List<Jugador> resultado = partidoTest.invitarJugadores(jugadoresDisponibles);
            
            assertFalse(resultado.isEmpty());
            // El jugador cerca debería estar antes que el lejano
            assertTrue(resultado.contains(jugadorCerca));
        }

        @Test
        @DisplayName("Debería encontrar jugadores usando estrategia por nivel")
        void deberiaEncontrarJugadoresUsandoEstrategiaNivel() {
            // Configurar partido con nivel intermedio
            partidoTest.setNivel(Nivel.INTERMEDIO);
            
            // Crear jugadores con diferentes niveles
            DeporteJugador dj1 = new DeporteJugador();
            dj1.setJugador(jugadorTest1);
            dj1.setDeporte(deporteTest);
            dj1.setNivel(Nivel.INTERMEDIO);
            jugadorTest1.getDeportes().add(dj1);
            
            DeporteJugador dj2 = new DeporteJugador();
            dj2.setJugador(jugadorTest2);
            dj2.setDeporte(deporteTest);
            dj2.setNivel(Nivel.PRINCIPIANTE);
            jugadorTest2.getDeportes().add(dj2);
            
            List<Jugador> jugadoresDisponibles = Arrays.asList(jugadorTest1, jugadorTest2);
            
            partidoTest.cambiarEstrategiaEmparejamiento(new EmparejamientoPorNivel());
            List<Jugador> resultado = partidoTest.invitarJugadores(jugadoresDisponibles);
            
            // Jugador con nivel intermedio debería tener prioridad
            assertTrue(resultado.contains(jugadorTest1));
        }

        @Test
        @DisplayName("Debería encontrar jugadores usando estrategia por historial")
        void deberiaEncontrarJugadoresUsandoEstrategiaHistorial() {
            // Simular historial - jugadorTest1 ha jugado con organizador antes
            jugadorTest1.agregarAlHistorial(partidoTest);
            
            List<Jugador> jugadoresDisponibles = Arrays.asList(jugadorTest1, jugadorTest2);
            
            partidoTest.cambiarEstrategiaEmparejamiento(new EmparejamientoPorHistorial());
            List<Jugador> resultado = partidoTest.invitarJugadores(jugadoresDisponibles);
            
            assertFalse(resultado.isEmpty());
        }
    }

    @Nested
    @DisplayName("Tests de Observer Pattern")
    class ObserverPatternTests {

        @Test
        @DisplayName("Debería permitir agregar observadores")
        void deberiaPermitirAgregarObservadores() {
            Observer observer = new Observer() {
                @Override
                public void update(Partido partido) {
                    // Observer de test
                }
            };
            
            assertDoesNotThrow(() -> {
                partidoTest.attach(observer);
            });
        }

        @Test
        @DisplayName("Debería permitir remover observadores")
        void deberiaPermitirRemoverObservadores() {
            Observer observer = new Observer() {
                @Override
                public void update(Partido partido) {
                    // Observer de test
                }
            };
            
            partidoTest.attach(observer);
            
            assertDoesNotThrow(() -> {
                partidoTest.detach(observer);
            });
        }

        @Test
        @DisplayName("Debería notificar observadores al cambiar estado")
        void deberiaNotificarObservadoresAlCambiarEstado() {
            final boolean[] notificado = {false};
            
            Observer observer = new Observer() {
                @Override
                public void update(Partido partido) {
                    notificado[0] = true;
                }
            };
            
            partidoTest.attach(observer);
            partidoTest.cambiarEstado(new PartidoArmado());
            
            assertTrue(notificado[0]);
        }

        @Test
        @DisplayName("Debería manejar múltiples observadores")
        void deberiaManejarMultiplesObservadores() {
            final int[] contador = {0};
            
            Observer observer1 = new Observer() {
                @Override
                public void update(Partido partido) {
                    contador[0]++;
                }
            };
            
            Observer observer2 = new Observer() {
                @Override
                public void update(Partido partido) {
                    contador[0]++;
                }
            };
            
            partidoTest.attach(observer1);
            partidoTest.attach(observer2);
            partidoTest.cambiarEstado(new PartidoArmado());
            
            assertEquals(2, contador[0]);
        }
    }

    @Nested
    @DisplayName("Tests de Gestión de Jugadores")
    class GestionJugadoresTests {

        @Test
        @DisplayName("Debería agregar jugadores correctamente")
        void deberiaAgregarJugadoresCorrectamente() {
            partidoTest.agregarJugador(jugadorTest1);
            
            assertTrue(partidoTest.getJugadores().contains(jugadorTest1));
            assertEquals(1, partidoTest.getJugadores().size());
        }

        @Test
        @DisplayName("Debería remover jugadores correctamente")
        void deberiaRemoverJugadoresCorrectamente() {
            partidoTest.agregarJugador(jugadorTest1);
            assertTrue(partidoTest.getJugadores().contains(jugadorTest1));
            
            partidoTest.removerJugador(jugadorTest1);
            
            assertFalse(partidoTest.getJugadores().contains(jugadorTest1));
            assertEquals(0, partidoTest.getJugadores().size());
        }

        @Test
        @DisplayName("No debería agregar jugador null")
        void noDeberiaAgregarJugadorNull() {
            assertThrows(NullPointerException.class, () -> {
                partidoTest.agregarJugador(null);
            });
        }

        @Test
        @DisplayName("No debería remover jugador null")
        void noDeberiaRemoverJugadorNull() {
            assertThrows(NullPointerException.class, () -> {
                partidoTest.removerJugador(null);
            });
        }

        @Test
        @DisplayName("Debería confirmar asistencia de jugadores")
        void deberiaConfirmarAsistenciaDeJugadores() {
            partidoTest.agregarJugador(jugadorTest1);
            
            assertDoesNotThrow(() -> {
                partidoTest.confirmarAsistencia(jugadorTest1);
            });
            
            assertTrue(partidoTest.getJugadoresConfirmados().contains(jugadorTest1));
        }

        @Test
        @DisplayName("No debería confirmar asistencia de jugador no inscrito")
        void noDeberiaConfirmarAsistenciaDeJugadorNoInscrito() {
            assertThrows(IllegalArgumentException.class, () -> {
                partidoTest.confirmarAsistencia(jugadorTest1);
            });
        }

        @Test
        @DisplayName("Debería verificar si partido está completo")
        void deberiaVerificarSiPartidoEstaCompleto() {
            // Inicialmente no está completo
            assertFalse(partidoTest.estaCompleto());
            
            // Agregar suficientes jugadores para completar
            for (int i = 0; i < deporteTest.getCantidadJugadores(); i++) {
                Jugador jugador = new Jugador("Jugador" + i, "email" + i + "@test.com", "pass123", zonaTest);
                partidoTest.agregarJugador(jugador);
            }
            
            assertTrue(partidoTest.estaCompleto());
        }
    }

    @Nested
    @DisplayName("Tests de Validación y Propiedades")
    class ValidacionTests {

        @Test
        @DisplayName("Debería verificar si partido está en el futuro")
        void deberiaVerificarSiPartidoEstaEnElFuturo() {
            // Partido en el futuro
            partidoTest.setFechaHora(LocalDateTime.now().plusDays(1));
            assertTrue(partidoTest.estaEnElFuturo());
            
            // Partido en el pasado
            partidoTest.setFechaHora(LocalDateTime.now().minusDays(1));
            assertFalse(partidoTest.estaEnElFuturo());
        }

        @Test
        @DisplayName("Debería convertir a DTO correctamente")
        void deberiaConvertirADTOCorrectamente() {
            var dto = partidoTest.convertirADTO();
            
            assertEquals(partidoTest.getId(), dto.getId());
            assertEquals(partidoTest.getTitulo(), dto.getTitulo());
            assertEquals(partidoTest.getFechaHora(), dto.getFechaHora());
            assertEquals(partidoTest.getDuracionMinutos(), dto.getDuracionMinutos());
            assertNotNull(dto.getZonaDTO());
            assertNotNull(dto.getDeporteDTO());
        }

        @Test
        @DisplayName("Debería tener toString informativo")
        void deberiaTenerToStringInformativo() {
            String toString = partidoTest.toString();
            
            assertTrue(toString.contains(partidoTest.getTitulo()));
            assertTrue(toString.contains(partidoTest.getZona().getBarrio()));
            assertTrue(toString.contains(partidoTest.getDeporte().getNombre()));
        }

        @Test
        @DisplayName("Debería manejar estado null correctamente")
        void deberiaManejarEstadoNullCorrectamente() {
            assertThrows(IllegalStateException.class, () -> {
                partidoTest.cambiarEstado(null);
            });
        }
    }
} 