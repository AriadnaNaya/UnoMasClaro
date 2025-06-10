package com.tpo.unoMas.service;

import com.tpo.unoMas.dto.*;
import com.tpo.unoMas.model.*;
import com.tpo.unoMas.model.estado.*;
import com.tpo.unoMas.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio principal para gestión de partidos
 * Implementa los requerimientos del TPO con patrones de diseño
 */
@Service
@Transactional
public class PartidoService {

    @Autowired
    private PartidoRepository partidoRepository;
    
    @Autowired
    private JugadorRepository jugadorRepository;
    
    @Autowired
    private ZonaRepository zonaRepository;
    
    @Autowired
    private DeporteRepository deporteRepository;
    
    @Autowired
    private InvitacionService invitacionService;

    /**
     * RF3: Crear un partido
     * Estado inicial: NecesitamosJugadores
     */
    public Partido crearPartido(CrearPartidoRequest request) {
        // Validaciones
        Jugador organizador = jugadorRepository.findById(request.getOrganizadorId())
                .orElseThrow(() -> new RuntimeException("Organizador no encontrado"));
        
        Zona zona = zonaRepository.findById(request.getZonaId())
                .orElseThrow(() -> new RuntimeException("Zona no encontrada"));
        
        Deporte deporte = deporteRepository.findById(request.getDeporteId())
                .orElseThrow(() -> new RuntimeException("Deporte no encontrado"));

        if (request.getMinJugadores() > request.getMaxJugadores()) {
            throw new RuntimeException("Mínimo de jugadores no puede ser mayor al máximo");
        }

        // Crear partido
        Partido partido = new Partido();
        partido.setTitulo(request.getTitulo());
        partido.setFechaHora(request.getFechaHora());
        partido.setZona(zona);
        partido.setDeporte(deporte);
        partido.setNivel(request.getNivel());
        partido.setOrganizador(organizador);
        partido.setMinJugadores(request.getMinJugadores());
        partido.setMaxJugadores(request.getMaxJugadores());
        partido.setDuracionMinutos(request.getDuracionMinutos());
        
        // Estado inicial
        partido.setEstado(new NecesitamosJugadores());
        
        // Guardar y agregar organizador
        partido = partidoRepository.save(partido);
        partido.agregarJugador(organizador);
        
        // El InvitacionService como Observer enviará invitaciones automáticamente
        partido.notifyObservers();
        
        return partidoRepository.save(partido);
    }

    /**
     * RF2: Búsqueda de partidos
     */
    public List<Partido> buscarPartidos(BuscarPartidosRequest request) {
        List<Partido> partidos = partidoRepository.findAll();
        
        return partidos.stream()
                .filter(partido -> {
                    // Filtrar por zona
                    if (request.getZonaId() != null && 
                        !partido.getZona().getId().equals(request.getZonaId())) {
                        return false;
                    }
                    
                    // Filtrar por deporte
                    if (request.getDeporteId() != null && 
                        !partido.getDeporte().getId().equals(request.getDeporteId())) {
                        return false;
                    }
                    
                    // Filtrar por nivel
                    if (request.getNivel() != null && 
                        partido.getNivel() != request.getNivel()) {
                        return false;
                    }
                    
                    // Filtrar por rango de fechas
                    if (request.getFechaDesde() != null && 
                        partido.getFechaHora().isBefore(request.getFechaDesde())) {
                        return false;
                    }
                    
                    if (request.getFechaHasta() != null && 
                        partido.getFechaHora().isAfter(request.getFechaHasta())) {
                        return false;
                    }
                    
                    // Filtrar por espacios disponibles
                    if (request.getSoloConEspaciosDisponibles() && 
                        partido.getJugadores().size() >= partido.getMaxJugadores()) {
                        return false;
                    }
                    
                    // Filtrar por estado
                    if (request.getEstado() != null && 
                        !partido.getEstado().getClass().getSimpleName().equals(request.getEstado())) {
                        return false;
                    }
                    
                    return true;
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
    public void unirseAPartido(Long partidoId, Long jugadorId) {
        Partido partido = obtenerPorId(partidoId);
        Jugador jugador = jugadorRepository.findById(jugadorId)
                .orElseThrow(() -> new RuntimeException("Jugador no encontrado"));
        
        partido.agregarJugador(jugador);
        partidoRepository.save(partido);
    }

    /**
     * Salirse de un partido
     */
    public void salirseDePartido(Long partidoId, Long jugadorId) {
        Partido partido = obtenerPorId(partidoId);
        Jugador jugador = jugadorRepository.findById(jugadorId)
                .orElseThrow(() -> new RuntimeException("Jugador no encontrado"));
        
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
    public void confirmarParticipacion(Long partidoId, Long jugadorId) {
        Partido partido = obtenerPorId(partidoId);
        Jugador jugador = jugadorRepository.findById(jugadorId)
                .orElseThrow(() -> new RuntimeException("Jugador no encontrado"));
        
        if (!partido.getJugadores().contains(jugador)) {
            throw new RuntimeException("El jugador no está en este partido");
        }
        
        partido.confirmarAsistencia(jugador); // Usar método original existente
        partidoRepository.save(partido);
    }

    /**
     * Obtener partidos por jugador
     */
    public List<Partido> obtenerPartidosPorJugador(Long jugadorId) {
        Jugador jugador = jugadorRepository.findById(jugadorId)
                .orElseThrow(() -> new RuntimeException("Jugador no encontrado"));
        
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
        dto.setMinJugadores(partido.getMinJugadores());
        dto.setMaxJugadores(partido.getMaxJugadores());
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
        
        dto.setJugadoresNecesarios(partido.getMaxJugadores() - partido.getJugadores().size());
        
        return dto;
    }
} 