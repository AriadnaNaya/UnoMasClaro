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

@RestController
@RequestMapping("/api/jugadores")
@CrossOrigin(origins = "*")
@Tag(name = "👤 Jugadores", description = "Gestión de usuarios y perfiles de jugadores")
public class JugadorController {

    @Autowired
    private JugadorService jugadorService;

//  Registro de Jugadores.
    @PostMapping("/registro")
    public ResponseEntity<?> registrarJugador(@Valid @RequestBody RegistroJugadorRequest request) {
        try {
            Jugador jugador = new Jugador();
            jugador.setNombre(request.getNombre());
            jugador.setEmail(request.getEmail());
            jugador.setTelefono(request.getTelefono());
            jugador.setPassword(request.getPassword());

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


    @Operation(summary = "Agregar deporte a jugador", description = "Agrega un deporte al jugador con el nivel especificado. El deporte no se marca como favorito por defecto.")
    @ApiResponse(responseCode = "200", description = "Deporte agregado exitosamente", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n  'mensaje': 'Deporte agregado exitosamente',\n  'jugador': { ... }\n}")))
    @ApiResponse(responseCode = "400", description = "Error de validación", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n  'error': 'No se pudo agregar el deporte',\n  'detalle': 'El jugador ya tiene este deporte asociado'\n}")))
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n  'deporteId': 1,\n  'nivel': 'INTERMEDIO'\n}")))
    @PostMapping("/{id}/deportes")
    public ResponseEntity<?> agregarDeporte(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        try {
            Long deporteId = Long.valueOf(body.get("deporteId").toString());
            Nivel nivel = Nivel.valueOf(body.get("nivel").toString());
            Jugador jugador = jugadorService.agregarDeporte(id, deporteId, nivel);
            return ResponseEntity.ok(Map.of(
                    "mensaje", "Deporte agregado exitosamente",
                    "jugador", jugador.convertirADTO()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "No se pudo agregar el deporte",
                    "detalle", e.getMessage()
            ));
        }
    }



    //  Eliminar Deporte de jugador
    @Operation(summary = "Eliminar deporte", description = "Elimina un deporte favorito del jugador (lo marca como no favorito, no borra la relación).")
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


//  Actualizar el nivel de un deporte  del jugador
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

    /**
     * Modificar el estado de favorito de un deporte
     */
    @Operation(summary = "Modificar estado de favorito", description = "Modifica si un deporte es favorito o no para el jugador.")
    @ApiResponse(responseCode = "200", description = "Estado de favorito modificado", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n  'mensaje': 'Estado de favorito modificado',\n  'jugador': { ... }\n}")))
    @ApiResponse(responseCode = "400", description = "Error de validación", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n  'error': 'No se pudo modificar el estado de favorito',\n  'detalle': 'El jugador no tiene este deporte asociado'\n}")))
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        required = true,
        content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n  'esFavorito': true\n}")))
    @PutMapping("/{id}/deportes/{deporteId}/favorito")
    public ResponseEntity<?> modificarEstadoFavorito(@PathVariable Long id, @PathVariable Long deporteId, @RequestBody Map<String, Object> body) {
        try {
            boolean esFavorito = Boolean.valueOf(body.get("esFavorito").toString());
            Jugador jugador = jugadorService.modificarEstadoFavorito(id, deporteId, esFavorito);
            return ResponseEntity.ok(Map.of(
                "mensaje", "Estado de favorito modificado",
                "jugador", jugador.convertirADTO()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "No se pudo modificar el estado de favorito",
                "detalle", e.getMessage()
            ));
        }
    }
} 