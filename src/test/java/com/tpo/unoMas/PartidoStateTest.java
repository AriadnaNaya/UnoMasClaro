package com.tpo.unoMas;

import com.tpo.unoMas.model.*;
import com.tpo.unoMas.model.estado.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

@DisplayName("Tests del Patrón State en Partido")
public class PartidoStateTest {

    private Partido partido;
    private Jugador jugador1;
    private Jugador jugador2;
    private Jugador jugador3;
    private Jugador organizador;
    private Zona zona;
    private Deporte deporte;

    @BeforeEach
    void setUp() {
        // Crear objetos mock para las pruebas
        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setLatitud(-34.6036840);
        ubicacion.setLongitud(-58.3815591);
        
        zona = new Zona("Centro", "CABA", ubicacion);
        
        deporte = new Deporte("Fútbol", "Deporte de equipo");
        
        // Crear jugadores
        organizador = new Jugador();
        organizador.setNombre("Organizador");
        organizador.setEmail("org@test.com");
        organizador.setPassword("password123");
        organizador.setZona(zona);
        
        jugador1 = new Jugador();
        jugador1.setNombre("Jugador 1");
        jugador1.setEmail("j1@test.com");
        jugador1.setPassword("password123");
        jugador1.setZona(zona);
        
        jugador2 = new Jugador();
        jugador2.setNombre("Jugador 2");
        jugador2.setEmail("j2@test.com");
        jugador2.setPassword("password123");
        jugador2.setZona(zona);
        
        jugador3 = new Jugador();
        jugador3.setNombre("Jugador 3");
        jugador3.setEmail("j3@test.com");
        jugador3.setPassword("password123");
        jugador3.setZona(zona);
        
        // Crear partido
        partido = new Partido();
        partido.setTitulo("Partido de Prueba");
        partido.setFechaHora(LocalDateTime.now().plusDays(1));
        partido.setZona(zona);
        partido.setDeporte(deporte);
        partido.setNivel(Nivel.PRINCIPIANTE);
        partido.setOrganizador(organizador);
        partido.setMinJugadores(2);
        partido.setMaxJugadores(4);
        partido.setDuracionMinutos(90);
    }

    @Test
    @DisplayName("Estado inicial debe ser NecesitamosJugadores")
    void testEstadoInicial() {
        System.out.println("=== TEST: Estado Inicial ===");
        assertTrue(partido.getEstado() instanceof NecesitamosJugadores);
        System.out.println("✓ Estado inicial: " + partido.getEstado().getClass().getSimpleName());
        assertEquals(0, partido.getJugadores().size());
        System.out.println("✓ Cantidad de jugadores: " + partido.getJugadores().size());
    }

    @Test
    @DisplayName("Transición de NecesitamosJugadores a PartidoArmado")
    void testTransicionAPartidoArmado() {
        System.out.println("\n=== TEST: Transición a PartidoArmado ===");
        
        // Estado inicial
        System.out.println("Estado inicial: " + partido.getEstado().getClass().getSimpleName());
        
        // Agregar primer jugador (aún necesita más)
        partido.agregarJugador(jugador1);
        System.out.println("Después de agregar jugador 1:");
        System.out.println("  - Estado: " + partido.getEstado().getClass().getSimpleName());
        System.out.println("  - Jugadores: " + partido.getJugadores().size());
        assertTrue(partido.getEstado() instanceof NecesitamosJugadores);
        
        // Agregar segundo jugador (alcanza el mínimo)
        partido.agregarJugador(jugador2);
        System.out.println("Después de agregar jugador 2:");
        System.out.println("  - Estado: " + partido.getEstado().getClass().getSimpleName());
        System.out.println("  - Jugadores: " + partido.getJugadores().size());
        assertTrue(partido.getEstado() instanceof PartidoArmado);
        assertEquals(2, partido.getJugadores().size());
    }

    @Test
    @DisplayName("Transición de PartidoArmado de vuelta a NecesitamosJugadores")
    void testTransicionDeVueltaANecesitamosJugadores() {
        System.out.println("\n=== TEST: Vuelta a NecesitamosJugadores ===");
        
        // Llegar a PartidoArmado
        partido.agregarJugador(jugador1);
        partido.agregarJugador(jugador2);
        assertTrue(partido.getEstado() instanceof PartidoArmado);
        System.out.println("Estado PartidoArmado alcanzado con " + partido.getJugadores().size() + " jugadores");
        
        // Remover un jugador (queda por debajo del mínimo)
        partido.removerJugador(jugador2);
        System.out.println("Después de remover jugador:");
        System.out.println("  - Estado: " + partido.getEstado().getClass().getSimpleName());
        System.out.println("  - Jugadores: " + partido.getJugadores().size());
        assertTrue(partido.getEstado() instanceof NecesitamosJugadores);
        assertEquals(1, partido.getJugadores().size());
    }

    @Test
    @DisplayName("Transición de PartidoArmado a Confirmado")
    void testTransicionAConfirmado() {
        System.out.println("\n=== TEST: Transición a Confirmado ===");
        
        // Llegar a PartidoArmado
        partido.agregarJugador(jugador1);
        partido.agregarJugador(jugador2);
        assertTrue(partido.getEstado() instanceof PartidoArmado);
        System.out.println("Estado PartidoArmado con " + partido.getJugadores().size() + " jugadores");
        
        // Confirmar asistencia de todos los jugadores
        partido.confirmarAsistencia(jugador1);
        System.out.println("Confirmado jugador 1. Estado: " + partido.getEstado().getClass().getSimpleName());
        assertTrue(partido.getEstado() instanceof PartidoArmado); // Aún no todos confirmados
        
        partido.confirmarAsistencia(jugador2);
        System.out.println("Confirmado jugador 2. Estado: " + partido.getEstado().getClass().getSimpleName());
        assertTrue(partido.getEstado() instanceof Confirmado); // Ahora sí está confirmado
    }

    @Test
    @DisplayName("Transición de Confirmado a EnJuego")
    void testTransicionAEnJuego() {
        System.out.println("\n=== TEST: Transición a EnJuego ===");
        
        // Llegar a Confirmado
        partido.agregarJugador(jugador1);
        partido.agregarJugador(jugador2);
        partido.confirmarAsistencia(jugador1);
        partido.confirmarAsistencia(jugador2);
        assertTrue(partido.getEstado() instanceof Confirmado);
        System.out.println("Estado Confirmado alcanzado");
        
        // Iniciar partido
        partido.iniciar();
        System.out.println("Después de iniciar:");
        System.out.println("  - Estado: " + partido.getEstado().getClass().getSimpleName());
        assertTrue(partido.getEstado() instanceof EnJuego);
    }

    @Test
    @DisplayName("Transición de EnJuego a Finalizado")
    void testTransicionAFinalizado() {
        System.out.println("\n=== TEST: Transición a Finalizado ===");
        
        // Llegar a EnJuego
        partido.agregarJugador(jugador1);
        partido.agregarJugador(jugador2);
        partido.confirmarAsistencia(jugador1);
        partido.confirmarAsistencia(jugador2);
        partido.iniciar();
        assertTrue(partido.getEstado() instanceof EnJuego);
        System.out.println("Estado EnJuego alcanzado");
        
        // Finalizar partido
        partido.finalizar();
        System.out.println("Después de finalizar:");
        System.out.println("  - Estado: " + partido.getEstado().getClass().getSimpleName());
        assertTrue(partido.getEstado() instanceof Finalizado);
    }

    @Test
    @DisplayName("Transición a Cancelado desde PartidoArmado")
    void testTransicionACancelado() {
        System.out.println("\n=== TEST: Transición a Cancelado ===");
        
        // Llegar a PartidoArmado
        partido.agregarJugador(jugador1);
        partido.agregarJugador(jugador2);
        assertTrue(partido.getEstado() instanceof PartidoArmado);
        System.out.println("Estado PartidoArmado alcanzado");
        
        // Cancelar partido
        partido.cancelar();
        System.out.println("Después de cancelar:");
        System.out.println("  - Estado: " + partido.getEstado().getClass().getSimpleName());
        assertTrue(partido.getEstado() instanceof Cancelado);
    }

    @Test
    @DisplayName("Ciclo completo de vida del partido")
    void testCicloCompleto() {
        System.out.println("\n=== TEST: Ciclo Completo ===");
        
        // 1. Estado inicial
        System.out.println("1. Estado inicial: " + partido.getEstado().getClass().getSimpleName());
        assertTrue(partido.getEstado() instanceof NecesitamosJugadores);
        
        // 2. Agregar jugadores hasta alcanzar mínimo
        partido.agregarJugador(jugador1);
        partido.agregarJugador(jugador2);
        System.out.println("2. Después de agregar jugadores: " + partido.getEstado().getClass().getSimpleName());
        assertTrue(partido.getEstado() instanceof PartidoArmado);
        
        // 3. Confirmar asistencias
        partido.confirmarAsistencia(jugador1);
        partido.confirmarAsistencia(jugador2);
        System.out.println("3. Después de confirmar asistencias: " + partido.getEstado().getClass().getSimpleName());
        assertTrue(partido.getEstado() instanceof Confirmado);
        
        // 4. Iniciar partido
        partido.iniciar();
        System.out.println("4. Después de iniciar: " + partido.getEstado().getClass().getSimpleName());
        assertTrue(partido.getEstado() instanceof EnJuego);
        
        // 5. Finalizar partido
        partido.finalizar();
        System.out.println("5. Después de finalizar: " + partido.getEstado().getClass().getSimpleName());
        assertTrue(partido.getEstado() instanceof Finalizado);
        
        System.out.println("✓ Ciclo completo exitoso!");
    }

    @Test
    @DisplayName("Excepciones en estados incorrectos")
    void testExcepcionesEstadosIncorrectos() {
        System.out.println("\n=== TEST: Excepciones en Estados Incorrectos ===");
        
        // Intentar iniciar sin estar confirmado
        assertThrows(IllegalStateException.class, () -> partido.iniciar());
        System.out.println("✓ Excepción correcta al intentar iniciar sin confirmar");
        
        // Intentar finalizar sin estar en juego
        assertThrows(IllegalStateException.class, () -> partido.finalizar());
        System.out.println("✓ Excepción correcta al intentar finalizar sin estar en juego");
        
        // Intentar confirmar sin tener jugadores suficientes
        assertThrows(IllegalStateException.class, () -> {
            EstadoPartido estado = new NecesitamosJugadores();
            estado.confirmarPartido(partido);
        });
        System.out.println("✓ Excepción correcta al intentar confirmar sin jugadores suficientes");
    }

    @Test
    @DisplayName("Prueba con capacidad máxima")
    void testCapacidadMaxima() {
        System.out.println("\n=== TEST: Capacidad Máxima ===");
        
        // Llenar hasta capacidad máxima
        partido.agregarJugador(jugador1);
        partido.agregarJugador(jugador2);
        assertTrue(partido.getEstado() instanceof PartidoArmado);
        System.out.println("Estado después de 2 jugadores: " + partido.getEstado().getClass().getSimpleName());
        
        // Agregar hasta el máximo
        partido.agregarJugador(jugador3);
        partido.agregarJugador(organizador);
        assertEquals(4, partido.getJugadores().size());
        System.out.println("Jugadores en partido: " + partido.getJugadores().size() + "/" + partido.getMaxJugadores());
        assertTrue(partido.estaCompleto());
        System.out.println("✓ Partido completo alcanzado");
    }
} 