package com.tpo.unoMas.controller;

import com.tpo.unoMas.dto.RegistroJugadorRequest;
import com.tpo.unoMas.dto.JugadorDTO;
import com.tpo.unoMas.model.Jugador;
import com.tpo.unoMas.model.Nivel;
import com.tpo.unoMas.service.JugadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;

/**
 * Controller REST para gestión de jugadores/usuarios
 * Implementa el requerimiento RF1: Registro de usuarios
 */
@RestController
@RequestMapping("/api/jugadores")
@CrossOrigin(origins = "*")
@Tag(name = "👤 Jugadores", description = "Gestión de usuarios y perfiles de jugadores")
public class JugadorController {

    @Autowired
    private JugadorService jugadorService;

    /**
     * RF1: Registro de usuarios
     * Un usuario debe poder registrarse al sistema
     */
    @PostMapping("/registro")
    public ResponseEntity<?> registrarJugador(@Valid @RequestBody RegistroJugadorRequest request) {
        try {
            Jugador jugador = new Jugador();
            jugador.setNombre(request.getNombre());
            jugador.setEmail(request.getEmail());
            jugador.setNivel(request.getNivel());
            jugador.setTelefono(request.getTelefono());
            // Setear zona y otros campos si corresponde
            Jugador jugadorGuardado = jugadorService.guardar(jugador);
            JugadorDTO jugadorDTO = jugadorGuardado.convertirADTO();
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "mensaje", "Jugador registrado exitosamente",
                "jugador", jugadorDTO
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Error al registrar jugador",
                "detalle", e.getMessage()
            ));
        }
    }

    /**
     * Obtener jugador por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerJugador(@PathVariable Long id) {
        try {
            Jugador jugador = jugadorService.obtenerPorId(id);
            JugadorDTO jugadorDTO = jugador.convertirADTO();
            return ResponseEntity.ok(Map.of(
                "jugador", jugadorDTO
            ));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Obtener todos los jugadores
     */
    @GetMapping
    public ResponseEntity<?> obtenerTodosLosJugadores() {
        try {
            List<Jugador> jugadores = jugadorService.obtenerTodos();
            List<JugadorDTO> jugadoresDTO = jugadores.stream()
                .map(Jugador::convertirADTO)
                .toList();
            return ResponseEntity.ok(Map.of(
                "jugadores", jugadoresDTO,
                "cantidad", jugadoresDTO.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Error al obtener jugadores",
                "detalle", e.getMessage()
            ));
        }
    }

    /**
     * Buscar jugadores por zona
     */
    @GetMapping("/zona/{zonaId}")
    public ResponseEntity<?> obtenerJugadoresPorZona(@PathVariable Long zonaId) {
        try {
            List<Jugador> jugadores = jugadorService.obtenerPorZona(zonaId);
            List<JugadorDTO> jugadoresDTO = jugadores.stream()
                .map(Jugador::convertirADTO)
                .toList();
            return ResponseEntity.ok(Map.of(
                "jugadores", jugadoresDTO,
                "zona", zonaId,
                "cantidad", jugadoresDTO.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Error al obtener jugadores por zona",
                "detalle", e.getMessage()
            ));
        }
    }

    /**
     * Buscar jugador por email
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<?> obtenerJugadorPorEmail(@PathVariable String email) {
        try {
            Jugador jugador = jugadorService.obtenerPorEmail(email);
            JugadorDTO jugadorDTO = jugador.convertirADTO();
            return ResponseEntity.ok(Map.of(
                "jugador", jugadorDTO
            ));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Actualizar perfil de jugador
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarJugador(@PathVariable Long id, 
                                             @Valid @RequestBody RegistroJugadorRequest request) {
        try {
            Jugador jugador = jugadorService.actualizarJugador(id, request);
            JugadorDTO jugadorDTO = jugador.convertirADTO();
            
            return ResponseEntity.ok(Map.of(
                "mensaje", "Jugador actualizado exitosamente",
                "jugador", jugadorDTO
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Error al actualizar jugador",
                "detalle", e.getMessage()
            ));
        }
    }

    /**
     * Agregar un deporte favorito al jugador
     */
    @Operation(summary = "Agregar deporte favorito", description = "Agrega un deporte favorito al jugador con el nivel especificado. Lanza error si ya es favorito.")
    @ApiResponse(responseCode = "200", description = "Deporte favorito agregado", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n  'mensaje': 'Deporte favorito agregado',\n  'jugador': { ... }\n}")))
    @ApiResponse(responseCode = "400", description = "Error de validación", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n  'error': 'No se pudo agregar deporte favorito',\n  'detalle': 'El deporte ya está marcado como favorito para este jugador'\n}")))
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        required = true,
        content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n  'deporteId': 1,\n  'nivel': 'INTERMEDIO'\n}")))
    @PostMapping("/{id}/deportes-favoritos")
    public ResponseEntity<?> agregarDeporteFavorito(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        try {
            Long deporteId = Long.valueOf(body.get("deporteId").toString());
            Nivel nivel = Nivel.valueOf(body.get("nivel").toString());
            Jugador jugador = jugadorService.agregarDeporteFavorito(id, deporteId, nivel);
            return ResponseEntity.ok(Map.of(
                "mensaje", "Deporte favorito agregado",
                "jugador", jugador.convertirADTO()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "No se pudo agregar deporte favorito",
                "detalle", e.getMessage()
            ));
        }
    }

    /**
     * Eliminar un deporte favorito del jugador
     */
    @Operation(summary = "Eliminar deporte favorito", description = "Elimina un deporte favorito del jugador (lo marca como no favorito, no borra la relación).")
    @ApiResponse(responseCode = "200", description = "Deporte favorito eliminado", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n  'mensaje': 'Deporte favorito eliminado',\n  'jugador': { ... }\n}")))
    @ApiResponse(responseCode = "400", description = "Error de validación", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n  'error': 'No se pudo eliminar deporte favorito',\n  'detalle': '...'\n}")))
    @DeleteMapping("/{id}/deportes-favoritos/{deporteId}")
    public ResponseEntity<?> eliminarDeporteFavorito(@PathVariable Long id, @PathVariable Long deporteId) {
        try {
            Jugador jugador = jugadorService.eliminarDeporteFavorito(id, deporteId);
            return ResponseEntity.ok(Map.of(
                "mensaje", "Deporte favorito eliminado",
                "jugador", jugador.convertirADTO()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "No se pudo eliminar deporte favorito",
                "detalle", e.getMessage()
            ));
        }
    }

    /**
     * Actualizar el nivel de un deporte favorito del jugador
     */
    @Operation(summary = "Actualizar nivel de deporte favorito", description = "Actualiza el nivel de un deporte favorito del jugador. Lanza error si el deporte no está asociado.")
    @ApiResponse(responseCode = "200", description = "Nivel de deporte actualizado", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n  'mensaje': 'Nivel de deporte actualizado',\n  'jugador': { ... }\n}")))
    @ApiResponse(responseCode = "400", description = "Error de validación", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n  'error': 'No se pudo actualizar el nivel del deporte',\n  'detalle': '...'\n}")))
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        required = true,
        content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n  'nivel': 'AVANZADO'\n}")))
    @PutMapping("/{id}/deportes-favoritos/{deporteId}/nivel")
    public ResponseEntity<?> actualizarNivelDeporte(@PathVariable Long id, @PathVariable Long deporteId, @RequestBody Map<String, Object> body) {
        try {
            Nivel nivel = Nivel.valueOf(body.get("nivel").toString());
            Jugador jugador = jugadorService.actualizarNivelDeporte(id, deporteId, nivel);
            return ResponseEntity.ok(Map.of(
                "mensaje", "Nivel de deporte actualizado",
                "jugador", jugador.convertirADTO()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "No se pudo actualizar el nivel del deporte",
                "detalle", e.getMessage()
            ));
        }
    }

} 