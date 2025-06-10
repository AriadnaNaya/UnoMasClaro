package com.tpo.unoMas.controller;

import com.tpo.unoMas.dto.PartidoDTO;
import com.tpo.unoMas.dto.CrearPartidoRequest;
import com.tpo.unoMas.dto.BuscarPartidosRequest;
import com.tpo.unoMas.model.Partido;
import com.tpo.unoMas.model.Jugador;
import com.tpo.unoMas.service.PartidoService;
import com.tpo.unoMas.service.InvitacionService;
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

/**
 * Controller REST para gestión de partidos
 * Implementa los requerimientos del TPO:
 * - RF2: Búsqueda de partidos
 * - RF3: Creación de un partido
 * - RF4: Estado de los partidos
 */
@RestController
@RequestMapping("/api/partidos")
@CrossOrigin(origins = "*")
@Tag(name = "⚽ Partidos", description = "Gestión completa de partidos deportivos con patrones de diseño")
public class PartidoController {

    @Autowired
    private PartidoService partidoService;
    
    @Autowired
    private InvitacionService invitacionService;

    /**
     * RF3: Crear un partido
     * El encuentro recién creado pasará a estar en el estado inicial: "Necesitamos jugadores"
     */
    @PostMapping
    @Operation(
        summary = "🎯 Crear nuevo partido deportivo",
        description = """
            **Crea un nuevo partido deportivo** y activa automáticamente todos los patrones de diseño:
            
            - 🎯 **State Pattern**: Inicia en estado `NecesitamosJugadores`
            - 👀 **Observer Pattern**: Dispara notificaciones automáticas
            - 🔧 **Strategy Pattern**: Ejecuta algoritmo de emparejamiento
            - 🔌 **Adapter Pattern**: Envía invitaciones por múltiples canales
            
            **Flujo automático tras creación:**
            1. Se agrega el organizador automáticamente
            2. Se envían invitaciones usando estrategia por defecto
            3. Se notifica a observadores suscritos
            """
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "✅ Partido creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "❌ Error en los datos de entrada"),
        @ApiResponse(responseCode = "404", description = "❌ Organizador, zona o deporte no encontrado")
    })
    public ResponseEntity<?> crearPartido(@Valid @RequestBody CrearPartidoRequest request) {
        try {
            Partido partido = partidoService.crearPartido(request);
            
            // El InvitacionService como Observer enviará invitaciones automáticamente
            PartidoDTO partidoDTO = partidoService.convertirADTO(partido);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "mensaje", "Partido creado exitosamente",
                "partido", partidoDTO,
                "estado", partido.getEstado().getClass().getSimpleName()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Error al crear partido",
                "detalle", e.getMessage()
            ));
        }
    }

    /**
     * RF2: Búsqueda de partidos
     * Los usuarios podrán buscar encuentros deportivos en su zona donde falten jugadores
     */
    @PostMapping("/buscar")
    public ResponseEntity<?> buscarPartidos(@Valid @RequestBody BuscarPartidosRequest request) {
        try {
            List<Partido> partidos = partidoService.buscarPartidos(request);
            List<PartidoDTO> partidosDTO = partidos.stream()
                .map(partidoService::convertirADTO)
                .toList();
                
            return ResponseEntity.ok(Map.of(
                "partidos", partidosDTO,
                "cantidad", partidosDTO.size(),
                "criterio", request.toString()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Error al buscar partidos",
                "detalle", e.getMessage()
            ));
        }
    }

    /**
     * Obtener partido por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPartido(@PathVariable Long id) {
        try {
            Partido partido = partidoService.obtenerPorId(id);
            PartidoDTO partidoDTO = partidoService.convertirADTO(partido);
            
            return ResponseEntity.ok(Map.of(
                "partido", partidoDTO,
                "estado", partido.getEstado().getClass().getSimpleName(),
                "jugadores", partido.getJugadores().size(),
                "necesita", partido.getMaxJugadores() - partido.getJugadores().size()
            ));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Unirse a un partido
     */
    @PostMapping("/{id}/unirse")
    public ResponseEntity<?> unirseAPartido(@PathVariable Long id, @RequestParam Long jugadorId) {
        try {
            partidoService.unirseAPartido(id, jugadorId);
            
            return ResponseEntity.ok(Map.of(
                "mensaje", "Te has unido al partido exitosamente"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "No se pudo unir al partido",
                "detalle", e.getMessage()
            ));
        }
    }

    /**
     * Salirse de un partido
     */
    @PostMapping("/{id}/salirse")
    public ResponseEntity<?> salirseDePartido(@PathVariable Long id, @RequestParam Long jugadorId) {
        try {
            partidoService.salirseDePartido(id, jugadorId);
            
            return ResponseEntity.ok(Map.of(
                "mensaje", "Te has salido del partido"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "No se pudo salir del partido",
                "detalle", e.getMessage()
            ));
        }
    }

    /**
     * Cancelar partido - Solo acción manual permitida para el organizador
     */
    @PostMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelarPartido(@PathVariable Long id, 
                                            @RequestParam Long organizadorId) {
        try {
            partidoService.cancelarPartido(id, organizadorId);
            
            return ResponseEntity.ok(Map.of(
                "mensaje", "Partido cancelado exitosamente"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "No se pudo cancelar el partido",
                "detalle", e.getMessage()
            ));
        }
    }

    /**
     * Confirmar participación en partido
     */
    @PostMapping("/{id}/confirmar")
    public ResponseEntity<?> confirmarParticipacion(@PathVariable Long id, @RequestParam Long jugadorId) {
        try {
            partidoService.confirmarParticipacion(id, jugadorId);
            
            return ResponseEntity.ok(Map.of(
                "mensaje", "Participación confirmada"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "No se pudo confirmar participación",
                "detalle", e.getMessage()
            ));
        }
    }

    /**
     * Obtener partidos por jugador
     */
    @GetMapping("/jugador/{jugadorId}")
    public ResponseEntity<?> obtenerPartidosPorJugador(@PathVariable Long jugadorId) {
        try {
            List<Partido> partidos = partidoService.obtenerPartidosPorJugador(jugadorId);
            List<PartidoDTO> partidosDTO = partidos.stream()
                .map(partidoService::convertirADTO)
                .toList();
                
            return ResponseEntity.ok(Map.of(
                "partidos", partidosDTO,
                "cantidad", partidosDTO.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Error al obtener partidos",
                "detalle", e.getMessage()
            ));
        }
    }

    // ELIMINADO: enviarInvitaciones manual
    // Las invitaciones se envían automáticamente cuando se crea un partido
    // a través del patrón Observer (InvitacionService)
} 