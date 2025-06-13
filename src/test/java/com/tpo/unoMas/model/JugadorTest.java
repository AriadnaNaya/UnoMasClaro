package com.tpo.unoMas.model;

import com.tpo.unoMas.dto.JugadorDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests exhaustivos para la entidad Jugador
 * Cubre: creación, validaciones, favoritos, DTO conversion, historial
 */
@DisplayName("Tests de Jugador")
class JugadorTest {

    private Validator validator;
    private Zona zonaTest;
    private Deporte deporteTest;
    private Jugador jugadorTest;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        
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
        
        // Crear jugador de test
        jugadorTest = new Jugador("Juan Pérez", "juan@email.com", "password123", zonaTest);
        jugadorTest.setTelefono("1234567890");
    }

    @Nested
    @DisplayName("Tests de Creación y Validación")
    class CreacionYValidacionTests {

        @Test
        @DisplayName("Debería crear jugador válido correctamente")
        void deberiaCrearJugadorValido() {
            Set<ConstraintViolation<Jugador>> violations = validator.validate(jugadorTest);
            assertTrue(violations.isEmpty(), "No debería haber violaciones de validación");
            
            assertEquals("Juan Pérez", jugadorTest.getNombre());
            assertEquals("juan@email.com", jugadorTest.getEmail());
            assertEquals("password123", jugadorTest.getPassword());
            assertEquals(zonaTest, jugadorTest.getZona());
            assertEquals("1234567890", jugadorTest.getTelefono());
        }

        @Test
        @DisplayName("Debería fallar con nombre vacío")
        void deberiaFallarConNombreVacio() {
            jugadorTest.setNombre("");
            Set<ConstraintViolation<Jugador>> violations = validator.validate(jugadorTest);
            
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("nombre no puede estar vacío")));
        }

        @Test
        @DisplayName("Debería fallar con nombre muy corto")
        void deberiaFallarConNombreMuyCorto() {
            jugadorTest.setNombre("A");
            Set<ConstraintViolation<Jugador>> violations = validator.validate(jugadorTest);
            
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("debe tener entre 2 y 100 caracteres")));
        }

        @Test
        @DisplayName("Debería fallar con email inválido")
        void deberiaFallarConEmailInvalido() {
            jugadorTest.setEmail("email-invalido");
            Set<ConstraintViolation<Jugador>> violations = validator.validate(jugadorTest);
            
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("email debe ser válido")));
        }

        @Test
        @DisplayName("Debería fallar con contraseña muy corta")
        void deberiaFallarConPasswordMuyCorta() {
            jugadorTest.setPassword("123");
            Set<ConstraintViolation<Jugador>> violations = validator.validate(jugadorTest);
            
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("debe tener al menos 6 caracteres")));
        }

        @Test
        @DisplayName("Debería fallar con zona nula")
        void deberiaFallarConZonaNula() {
            jugadorTest.setZona(null);
            Set<ConstraintViolation<Jugador>> violations = validator.validate(jugadorTest);
            
            assertFalse(violations.isEmpty());
            assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("zona no puede ser nula")));
        }
    }

    @Nested
    @DisplayName("Tests de Deportes Favoritos")
    class DeportesFavoritosTests {

        @BeforeEach
        void setUp() {
            // Agregar deporte al jugador
            DeporteJugador dj = new DeporteJugador();
            dj.setJugador(jugadorTest);
            dj.setDeporte(deporteTest);
            dj.setNivel(Nivel.INTERMEDIO);
            dj.setEsFavorito(false);
            jugadorTest.getDeportes().add(dj);
        }

        @Test
        @DisplayName("Debería agregar deporte a favoritos")
        void deberiaAgregarDeporteAFavoritos() {
            jugadorTest.agregarAFavoritos(deporteTest);
            
            assertTrue(jugadorTest.deportesFavoritos().contains(deporteTest));
            assertEquals(1, jugadorTest.deportesFavoritos().size());
        }

        @Test
        @DisplayName("Debería eliminar deporte de favoritos")
        void deberiaEliminarDeporteDeFavoritos() {
            jugadorTest.agregarAFavoritos(deporteTest);
            assertTrue(jugadorTest.deportesFavoritos().contains(deporteTest));
            
            jugadorTest.eliminarDeFavoritos(deporteTest);
            assertFalse(jugadorTest.deportesFavoritos().contains(deporteTest));
            assertEquals(0, jugadorTest.deportesFavoritos().size());
        }

        @Test
        @DisplayName("Debería manejar deportes favoritos vacíos")
        void deberiaManejarDeportesFavoritosVacios() {
            assertTrue(jugadorTest.deportesFavoritos().isEmpty());
        }

        @Test
        @DisplayName("Debería fallar con deporte null en favoritos")
        void deberiaFallarConDeporteNullEnFavoritos() {
            assertThrows(NullPointerException.class, () -> {
                jugadorTest.agregarAFavoritos(null);
            });
        }

        @Test
        @DisplayName("Debería fallar con deporte null al eliminar de favoritos")
        void deberiaFallarConDeporteNullAlEliminarDeFavoritos() {
            assertThrows(NullPointerException.class, () -> {
                jugadorTest.eliminarDeFavoritos(null);
            });
        }
    }

    @Nested
    @DisplayName("Tests de Conversión DTO")
    class ConversionDTOTests {

        @Test
        @DisplayName("Debería convertir a DTO correctamente")
        void deberiaConvertirADTOCorrectamente() {
            // Agregar deporte favorito
            DeporteJugador dj = new DeporteJugador();
            dj.setJugador(jugadorTest);
            dj.setDeporte(deporteTest);
            dj.setNivel(Nivel.INTERMEDIO);
            dj.setEsFavorito(true);
            jugadorTest.getDeportes().add(dj);
            
            JugadorDTO dto = jugadorTest.convertirADTO();
            
            assertEquals(jugadorTest.getId(), dto.getId());
            assertEquals(jugadorTest.getNombre(), dto.getNombre());
            assertEquals(jugadorTest.getEmail(), dto.getEmail());
            assertEquals(jugadorTest.getTelefono(), dto.getTelefono());
            assertNotNull(dto.getZonaDTO());
            assertEquals(1, dto.getDeportesFavoritos().size());
        }

        @Test
        @DisplayName("Debería manejar conversión DTO con zona nula")
        void deberiaManejarConversionDTOConZonaNula() {
            jugadorTest.setZona(null);
            
            JugadorDTO dto = jugadorTest.convertirADTO();
            
            assertNull(dto.getZonaDTO());
        }

        @Test
        @DisplayName("Debería manejar conversión DTO sin deportes")
        void deberiaManejarConversionDTOSinDeportes() {
            JugadorDTO dto = jugadorTest.convertirADTO();
            
            assertTrue(dto.getDeportesFavoritos().isEmpty());
        }
    }

    @Nested
    @DisplayName("Tests de Historial")
    class HistorialTests {

        private Partido partidoTest;

        @BeforeEach
        void setUp() {
            partidoTest = new Partido();
            partidoTest.setTitulo("Partido Test");
            partidoTest.setFechaHora(LocalDateTime.now().plusDays(1));
            partidoTest.setZona(zonaTest);
            partidoTest.setDeporte(deporteTest);
            partidoTest.setOrganizador(jugadorTest);
            partidoTest.setDuracionMinutos(90);
        }

        @Test
        @DisplayName("Debería inicializar historial vacío")
        void deberiaInicializarHistorialVacio() {
            Jugador jugadorNuevo = new Jugador("Nuevo", "nuevo@email.com", "password123", zonaTest);
            assertTrue(jugadorNuevo.getHistorial().isEmpty());
        }

        @Test
        @DisplayName("Debería agregar partido al historial")
        void deberiaAgregarPartidoAlHistorial() {
            Jugador jugadorNuevo = new Jugador("Nuevo", "nuevo@email.com", "password123", zonaTest);
            jugadorNuevo.agregarAlHistorial(partidoTest);
            
            assertEquals(1, jugadorNuevo.getHistorial().size());
            assertTrue(jugadorNuevo.getHistorial().contains(partidoTest));
        }

        @Test
        @DisplayName("Debería eliminar partido del historial")
        void deberiaEliminarPartidoDelHistorial() {
            Jugador jugadorNuevo = new Jugador("Nuevo", "nuevo@email.com", "password123", zonaTest);
            jugadorNuevo.agregarAlHistorial(partidoTest);
            assertEquals(1, jugadorNuevo.getHistorial().size());
            
            jugadorNuevo.eliminarDeHistorial(partidoTest);
            
            assertEquals(0, jugadorNuevo.getHistorial().size());
            assertFalse(jugadorNuevo.getHistorial().contains(partidoTest));
        }

        @Test
        @DisplayName("Debería permitir múltiples partidos en historial")
        void deberiaPermitirMultiplesPartidosEnHistorial() {
            Jugador jugadorNuevo = new Jugador("Nuevo", "nuevo@email.com", "password123", zonaTest);
            Partido partido2 = new Partido();
            partido2.setTitulo("Partido Test 2");
            partido2.setFechaHora(LocalDateTime.now().plusDays(2));
            partido2.setZona(zonaTest);
            partido2.setDeporte(deporteTest);
            partido2.setOrganizador(jugadorNuevo);
            partido2.setDuracionMinutos(90);
            
            jugadorNuevo.agregarAlHistorial(partidoTest);
            
            assertEquals(2, jugadorNuevo.getHistorial().size());
            assertTrue(jugadorNuevo.getHistorial().contains(partidoTest));
            assertTrue(jugadorNuevo.getHistorial().contains(partido2));
        }
    }

    @Nested
    @DisplayName("Tests de Setters y Getters")
    class SettersGettersTests {

        @Test
        @DisplayName("Debería modificar nombre correctamente")
        void deberiaModificarNombreCorrectamente() {
            String nuevoNombre = "María García";
            jugadorTest.setNombre(nuevoNombre);
            assertEquals(nuevoNombre, jugadorTest.getNombre());
        }

        @Test
        @DisplayName("Debería modificar email correctamente")
        void deberiaModificarEmailCorrectamente() {
            String nuevoEmail = "maria@email.com";
            jugadorTest.setEmail(nuevoEmail);
            assertEquals(nuevoEmail, jugadorTest.getEmail());
        }


        @Test
        @DisplayName("Debería modificar token FCM correctamente")
        void deberiaModificarTokenFCMCorrectamente() {
            String token = "test-fcm-token";
            jugadorTest.setTokenFCM(token);
            assertEquals(token, jugadorTest.getTokenFCM());
        }
    }
} 