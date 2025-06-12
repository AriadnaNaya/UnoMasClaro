package com.tpo.unoMas.service;

import com.tpo.unoMas.model.Jugador;
import com.tpo.unoMas.model.Zona;
import com.tpo.unoMas.model.Ubicacion;
import com.tpo.unoMas.repository.JugadorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests para JugadorService usando Mockito
 * Cubre: CRUD operations, validaciones de negocio, manejo de excepciones
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de JugadorService")
class JugadorServiceTest {

    @Mock
    private JugadorRepository jugadorRepository;

    @InjectMocks
    private JugadorService jugadorService;

    private Jugador jugadorTest;
    private Zona zonaTest;

    @BeforeEach
    void setUp() {
        // Crear zona de test
        zonaTest = new Zona();
        zonaTest.setBarrio("Palermo");
        zonaTest.setPartido("CABA");
        zonaTest.setUbicacion(new Ubicacion(-34.5875, -58.4217));
        
        // Crear jugador de test
        jugadorTest = new Jugador("Juan Pérez", "juan@email.com", "password123", zonaTest);
        jugadorTest.setTelefono("1234567890");
    }

    @Test
    @DisplayName("Debería crear jugador correctamente")
    void deberiaCrearJugadorCorrectamente() {
        // Arrange
        when(jugadorRepository.existsByEmail(jugadorTest.getEmail())).thenReturn(false);
        when(jugadorRepository.save(any(Jugador.class))).thenReturn(jugadorTest);

        // Act
        Jugador result = jugadorService.crearJugador(jugadorTest);

        // Assert
        assertNotNull(result);
        assertEquals(jugadorTest.getNombre(), result.getNombre());
        assertEquals(jugadorTest.getEmail(), result.getEmail());
        
        verify(jugadorRepository).existsByEmail(jugadorTest.getEmail());
        verify(jugadorRepository).save(jugadorTest);
    }

    @Test
    @DisplayName("No debería crear jugador con email duplicado")
    void noDeberiaCrearJugadorConEmailDuplicado() {
        // Arrange
        when(jugadorRepository.existsByEmail(jugadorTest.getEmail())).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            jugadorService.crearJugador(jugadorTest);
        });
        
        verify(jugadorRepository).existsByEmail(jugadorTest.getEmail());
        verify(jugadorRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debería obtener jugador por ID")
    void deberiaObtenerJugadorPorId() {
        // Arrange
        Long jugadorId = 1L;
        when(jugadorRepository.findById(jugadorId)).thenReturn(Optional.of(jugadorTest));

        // Act
        Optional<Jugador> result = jugadorService.obtenerJugadorPorId(jugadorId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(jugadorTest.getNombre(), result.get().getNombre());
        
        verify(jugadorRepository).findById(jugadorId);
    }

    @Test
    @DisplayName("Debería retornar empty cuando jugador no existe")
    void deberiaRetornarEmptyWhenJugadorNoExiste() {
        // Arrange
        Long jugadorId = 999L;
        when(jugadorRepository.findById(jugadorId)).thenReturn(Optional.empty());

        // Act
        Optional<Jugador> result = jugadorService.obtenerJugadorPorId(jugadorId);

        // Assert
        assertFalse(result.isPresent());
        
        verify(jugadorRepository).findById(jugadorId);
    }

    @Test
    @DisplayName("Debería obtener todos los jugadores")
    void deberiaObtenerTodosLosJugadores() {
        // Arrange
        Jugador jugador2 = new Jugador("María García", "maria@email.com", "password123", zonaTest);
        List<Jugador> jugadores = Arrays.asList(jugadorTest, jugador2);
        when(jugadorRepository.findAll()).thenReturn(jugadores);

        // Act
        List<Jugador> result = jugadorService.obtenerTodosLosJugadores();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(jugadorTest));
        assertTrue(result.contains(jugador2));
        
        verify(jugadorRepository).findAll();
    }

    @Test
    @DisplayName("Debería actualizar jugador existente")
    void deberiaActualizarJugadorExistente() {
        // Arrange
        Long jugadorId = 1L;
        Jugador jugadorActualizado = new Jugador("Juan Actualizado", "juan.nuevo@email.com", "newpass123", zonaTest);
        
        when(jugadorRepository.findById(jugadorId)).thenReturn(Optional.of(jugadorTest));
        when(jugadorRepository.save(any(Jugador.class))).thenReturn(jugadorActualizado);

        // Act
        Optional<Jugador> result = jugadorService.actualizarJugador(jugadorId, jugadorActualizado);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Juan Actualizado", result.get().getNombre());
        
        verify(jugadorRepository).findById(jugadorId);
        verify(jugadorRepository).save(any(Jugador.class));
    }

    @Test
    @DisplayName("No debería actualizar jugador inexistente")
    void noDeberiaActualizarJugadorInexistente() {
        // Arrange
        Long jugadorId = 999L;
        Jugador jugadorActualizado = new Jugador("Nuevo", "nuevo@email.com", "pass123", zonaTest);
        
        when(jugadorRepository.findById(jugadorId)).thenReturn(Optional.empty());

        // Act
        Optional<Jugador> result = jugadorService.actualizarJugador(jugadorId, jugadorActualizado);

        // Assert
        assertFalse(result.isPresent());
        
        verify(jugadorRepository).findById(jugadorId);
        verify(jugadorRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debería eliminar jugador existente")
    void deberiaEliminarJugadorExistente() {
        // Arrange
        Long jugadorId = 1L;
        when(jugadorRepository.existsById(jugadorId)).thenReturn(true);
        doNothing().when(jugadorRepository).deleteById(jugadorId);

        // Act
        boolean result = jugadorService.eliminarJugador(jugadorId);

        // Assert
        assertTrue(result);
        
        verify(jugadorRepository).existsById(jugadorId);
        verify(jugadorRepository).deleteById(jugadorId);
    }

    @Test
    @DisplayName("No debería eliminar jugador inexistente")
    void noDeberiaEliminarJugadorInexistente() {
        // Arrange
        Long jugadorId = 999L;
        when(jugadorRepository.existsById(jugadorId)).thenReturn(false);

        // Act
        boolean result = jugadorService.eliminarJugador(jugadorId);

        // Assert
        assertFalse(result);
        
        verify(jugadorRepository).existsById(jugadorId);
        verify(jugadorRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Debería buscar jugadores por email")
    void deberiaBuscarJugadoresPorEmail() {
        // Arrange
        String email = "juan@email.com";
        when(jugadorRepository.findByEmail(email)).thenReturn(Optional.of(jugadorTest));

        // Act
        Optional<Jugador> result = jugadorService.buscarPorEmail(email);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());
        
        verify(jugadorRepository).findByEmail(email);
    }

    @Test
    @DisplayName("Debería buscar jugadores por zona")
    void deberiaBuscarJugadoresPorZona() {
        // Arrange
        Jugador jugador2 = new Jugador("María García", "maria@email.com", "password123", zonaTest);
        List<Jugador> jugadores = Arrays.asList(jugadorTest, jugador2);
        when(jugadorRepository.findByZona(zonaTest)).thenReturn(jugadores);

        // Act
        List<Jugador> result = jugadorService.buscarPorZona(zonaTest);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(jugadorTest));
        assertTrue(result.contains(jugador2));
        
        verify(jugadorRepository).findByZona(zonaTest);
    }

    @Test
    @DisplayName("Debería validar jugador antes de crear")
    void deberiaValidarJugadorAntesDeCrear() {
        // Arrange
        Jugador jugadorInvalido = new Jugador("", "email-invalido", "123", null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            jugadorService.crearJugador(jugadorInvalido);
        });
        
        verify(jugadorRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debería manejar excepciones del repositorio")
    void deberiaManejarExcepcionesDelRepositorio() {
        // Arrange
        when(jugadorRepository.existsByEmail(jugadorTest.getEmail())).thenReturn(false);
        when(jugadorRepository.save(any(Jugador.class))).thenThrow(new RuntimeException("Error de BD"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            jugadorService.crearJugador(jugadorTest);
        });
        
        verify(jugadorRepository).existsByEmail(jugadorTest.getEmail());
        verify(jugadorRepository).save(jugadorTest);
    }
} 