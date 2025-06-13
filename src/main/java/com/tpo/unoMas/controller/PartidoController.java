package com.tpo.unoMas.controller;

import com.tpo.unoMas.dto.PartidoDTO;
import com.tpo.unoMas.dto.CrearPartidoRequest;
import com.tpo.unoMas.model.Partido;
import com.tpo.unoMas.model.Jugador;
import com.tpo.unoMas.model.estado.NecesitamosJugadores;
import com.tpo.unoMas.service.PartidoService;
import com.tpo.unoMas.service.JugadorService;
import com.tpo.unoMas.repository.ZonaRepository;
import com.tpo.unoMas.repository.DeporteRepository;
import com.tpo.unoMas.model.Zona;
import com.tpo.unoMas.model.Deporte;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import io.swagger.v3.oas.annotations.Operation;
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
    private JugadorService jugadorService;
    
    @Autowired
    private ZonaRepository zonaRepository;
    
    @Autowired
    private DeporteRepository deporteRepository;
    

    @PostMapping
    @Operation(
        summary = "🎯 Crear nuevo partido deportivo"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Partido creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Error en los datos de entrada"),
        @ApiResponse(responseCode = "404", description = "Organizador, zona o deporte no encontrado")
    })
    public ResponseEntity<?> crearPartido(@Valid @RequestBody CrearPartidoRequest request) {
        try {
            Jugador organizador = jugadorService.obtenerPorId(request.getOrganizadorId());
            Zona zona = zonaRepository.findById(request.getZonaId())
                .orElseThrow(() -> new RuntimeException("Zona no encontrada"));
            Deporte deporte = deporteRepository.findById(request.getDeporteId())
                .orElseThrow(() -> new RuntimeException("Deporte no encontrado"));
            List<Jugador> disponibles = jugadorService.obtenerDisponiblesParaPartido(
                request.getFechaHora(),
                request.getDuracionMinutos(),
                request.getOrganizadorId()
            );
            Partido partido = new Partido();
            partido.setTitulo(request.getTitulo());
            partido.setFechaHora(request.getFechaHora());
            partido.setZona(zona);
            partido.setDeporte(deporte);
            partido.setNivel(request.getNivel());

            //Ademas agrega el partido al historial del organizador
            partido.setOrganizador(organizador);
            partido.setDuracionMinutos(request.getDuracionMinutos());
            partido.cambiarEstado(new NecesitamosJugadores());

            partido.agregarJugador(organizador);

            //En partido service, este metodo usa la estrategia para enviar notificaciones a los jugadores que matchen
            //Envia notificacion usando el partron observer
            //Guarda entidad en repository
            Partido partidoGuardado = partidoService.guardarPartido(partido, disponibles);

            PartidoDTO partidoDTO = partidoGuardado.convertirADTO();

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "mensaje", "Partido creado exitosamente",
                "partido", partidoDTO,
                "estado", partidoGuardado.getEstado().getClass().getSimpleName()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Error al crear partido",
                "detalle", e.getMessage()
            ));
        }
    }

    //Muestra partidos para un jugador
    //La forma en la que busca es a traves de la strategia de emparejamiento de cada partido, de todos los partidos disponibles
    @PostMapping("/buscar")
    public ResponseEntity<?> buscarPartidos(Long jugadorID) {
        try {
            Jugador jugador = jugadorService.obtenerPorId(jugadorID);

            //Por cada partido, se fija si el jugador es compatible con su estrategia. Devuelve los compatibles
            List<Partido> partidos = partidoService.buscarPartidosCompatiblesParaJugador(jugador);

            List<PartidoDTO> partidosDTO = partidos.stream()
                .map(partidoService::convertirADTO)
                .toList();
                
            return ResponseEntity.ok(Map.of(
                "partidos", partidosDTO,
                "cantidad", partidosDTO.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Error al buscar partidos",
                "detalle", e.getMessage()
            ));
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPartido(@PathVariable Long id) {
        try {
            Partido partido = partidoService.obtenerPorId(id);
            PartidoDTO partidoDTO = partido.convertirADTO();
            return ResponseEntity.ok(Map.of(
                "partido", partidoDTO,
                "estado", partido.getEstado().getClass().getSimpleName(),
                "jugadores", partido.getJugadores().size(),
                "necesita", partido.getDeporte().getCantidadJugadores() - partido.getJugadores().size()
            ));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }


    //Cuando se crea un partido los jugadores compatibles reciben una invitacion
    //Esa invitacion deberia tener un boton que los derive a este endpoint.
    //Se usa el patron state para ver si el Jugador puede unirse al partido o no
    @PostMapping("/{id}/unirse")
    public ResponseEntity<?> unirseAPartido(@PathVariable Long id, @RequestParam Long jugadorId) {
        Jugador jugador = jugadorService.obtenerPorId(jugadorId);
        partidoService.unirseAPartido(id, jugador);
        return ResponseEntity.ok(Map.of(
            "mensaje", "Te has unido al partido exitosamente"
        ));
    }

    //Un jugador puede irse de un partido
    //Se usa el patron state para ver si el Jugador puede irse del partido o no
    @PostMapping("/{id}/salirse")
    public ResponseEntity<?> salirseDePartido(@PathVariable Long id, @RequestParam Long jugadorId) {
        Jugador jugador = jugadorService.obtenerPorId(jugadorId);
        partidoService.salirseDePartido(id, jugador);
        return ResponseEntity.ok(Map.of(
            "mensaje", "Te has salido del partido"
        ));
    }

    //Un organizador puede cancelar el partido
    //Se usa el patron state
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

    @PostMapping("/{id}/confirmar")
    public ResponseEntity<?> confirmarParticipacion(@PathVariable Long id, @RequestParam Long jugadorId) {
        Jugador jugador = jugadorService.obtenerPorId(jugadorId);
        partidoService.confirmarParticipacion(id, jugador);
        return ResponseEntity.ok(Map.of(
            "mensaje", "Participación confirmada"
        ));
    }


}