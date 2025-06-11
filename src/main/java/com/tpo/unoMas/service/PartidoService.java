package com.tpo.unoMas.service;

import com.tpo.unoMas.dto.*;
import com.tpo.unoMas.model.*;
import com.tpo.unoMas.model.estado.*;
import com.tpo.unoMas.repository.*;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;

import com.tpo.unoMas.model.strategy.emparejamiento.EmparejamientoPorCercania;
import com.tpo.unoMas.model.strategy.emparejamiento.EstrategiaEmparejamiento;
import com.tpo.unoMas.model.strategy.emparejamiento.EmparejamientoPorHistorial;

@Service
@Transactional
public class PartidoService {

    @Autowired
    private PartidoRepository partidoRepository;
    
    @Autowired
    private ZonaRepository zonaRepository;
    
    @Autowired
    private DeporteRepository deporteRepository;
    
    @Autowired
    private NotificacionService notificacionService;

    /**
     * RF3: Crear un partido
     * Estado inicial: NecesitamosJugadores
     */
    public Partido crearPartido(
        CrearPartidoRequest request,
        Jugador organizador,
        Zona zona,
        Deporte deporte,
        List<Jugador> disponibles
    ) {
        Partido partido = new Partido();
        partido.setTitulo(request.getTitulo());
        partido.setFechaHora(request.getFechaHora());
        partido.setZona(zona);
        partido.setDeporte(deporte);
        partido.setNivel(request.getNivel());
        partido.setOrganizador(organizador);
        partido.setDuracionMinutos(request.getDuracionMinutos());
        partido.cambiarEstado(new NecesitamosJugadores());
        partido = partidoRepository.save(partido);
        partido.agregarJugador(organizador);
        partido.invitarJugadores(disponibles);
        notificacionService.notificarConTitulo(partido, "Partido creado", "Se ha creado un partido");
        return partidoRepository.save(partido);
    }

    
    public List<Partido> buscarPartidos(BuscarPartidosRequest request) {
        List<Partido> todosLosPartidos = partidoRepository.findAll();
        return todosLosPartidos;
    }

    public List<Partido> buscarPartidosCompatiblesParaJugador(Long jugadorId) {
        Jugador jugador = obtenerPorId(jugadorId);
        List<Partido> todosLosPartidos = partidoRepository.findAll();
        return todosLosPartidos.stream()
            .filter(partido -> {
                EstrategiaEmparejamiento estrategia = partido.getEstrategiaEmparejamiento();
                if (estrategia == null) {
                    estrategia = new EmparejamientoPorCercania();
                }
                return estrategia.esCompatible(partido, jugador);
            })
            .collect(Collectors.toList());
    }

    /**
     * Obtener partido por ID
     */
    public Partido obtenerPorId(Long id) {
        return partidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Partido no encontrado"));
    }

    /**
     * Unirse a un partido
     */
    public void unirseAPartido(Long partidoId, Jugador jugador) {
        Partido partido = obtenerPorId(partidoId);
        partido.agregarJugador(jugador);
        partidoRepository.save(partido);
    }

    /**
     * Salirse de un partido
     */
    public void salirseDePartido(Long partidoId, Jugador jugador) {
        Partido partido = obtenerPorId(partidoId);
        partido.removerJugador(jugador);
        partidoRepository.save(partido);
    }

    /**
     * Cancelar partido - Única acción manual permitida para el organizador
     * Los demás cambios de estado son automáticos por el patrón State
     */
    public void cancelarPartido(Long partidoId, Long organizadorId) {
        Partido partido = obtenerPorId(partidoId);
        
        // Verificar que sea el organizador
        if (!partido.getOrganizador().getId().equals(organizadorId)) {
            throw new RuntimeException("Solo el organizador puede cancelar el partido");
        }
        
        try {
            // El estado interno validará si es posible cancelar
            partido.cancelar();
            partidoRepository.save(partido);
        } catch (Exception e) {
            throw new RuntimeException("No se puede cancelar el partido: " + e.getMessage());
        }
    }

    /**
     * Confirmar participación en partido
     */
    public void confirmarParticipacion(Long partidoId, Jugador jugador) {
        Partido partido = obtenerPorId(partidoId);
        if (!partido.getJugadores().contains(jugador)) {
            throw new RuntimeException("El jugador no está en este partido");
        }
        partido.confirmarAsistencia(jugador);
        partidoRepository.save(partido);
    }

    /**
     * Obtener partidos por jugador
     */
    public List<Partido> obtenerPartidosPorJugador(Long jugadorId) {
        Jugador jugador = obtenerPorId(jugadorId);
        return partidoRepository.findByJugadoresContaining(jugador);
    }

    // ELIMINADO: enviarInvitacionesConEstrategia
    // Las invitaciones se envían automáticamente por el patrón Observer
    // cuando se crea un partido. No necesitamos método manual adicional.

    /**
     * Convertir Partido a DTO
     */
    public PartidoDTO convertirADTO(Partido partido) {
        PartidoDTO dto = new PartidoDTO();
        dto.setId(partido.getId());
        dto.setTitulo(partido.getTitulo());
        dto.setFechaHora(partido.getFechaHora());
        dto.setZona(partido.getZona().getNombre());
        dto.setDeporte(partido.getDeporte().getNombre());
        dto.setNivel(partido.getNivel());
        dto.setOrganizador(partido.getOrganizador().getNombre());
        dto.setDuracionMinutos(partido.getDuracionMinutos());
        dto.setEstado(partido.getEstado().getClass().getSimpleName());
        
        // Convertir jugadores
        List<JugadorSimpleDTO> jugadoresDTO = partido.getJugadores().stream()
                .map(jugador -> new JugadorSimpleDTO(
                    jugador.getId(),
                    jugador.getNombre(),
                    jugador.getEmail(),
                    jugador.getZona().getNombre()
                ))
                .collect(Collectors.toList());
        dto.setJugadores(jugadoresDTO);
        
        // Convertir jugadores confirmados
        List<JugadorSimpleDTO> confirmadosDTO = partido.getJugadoresConfirmados().stream()
                .map(jugador -> new JugadorSimpleDTO(
                    jugador.getId(),
                    jugador.getNombre(),
                    jugador.getEmail(),
                    jugador.getZona().getNombre()
                ))
                .collect(Collectors.toList());
        dto.setJugadoresConfirmados(confirmadosDTO);
        
        dto.setJugadoresNecesarios(partido.getDeporte().getCantidadJugadores() - partido.getJugadores().size());
        
        return dto;
    }

    public List<Jugador> encontrarJugadoresPorHistorial(Partido partido, List<Jugador> jugadoresDisponibles, EmparejamientoPorHistorial estrategia) {
        return jugadoresDisponibles.stream()
            .filter(jugador -> {
                List<Partido> jugados = partidoRepository.findByJugadoresContaining(jugador);
                List<Partido> organizados = partidoRepository.findByOrganizador(jugador);
                Set<Partido> historial = new HashSet<>(jugados);
                historial.addAll(organizados);
                return estrategia.esCompatible(partido, new ArrayList<>(historial));
            })
            .collect(Collectors.toList());
    }

} 