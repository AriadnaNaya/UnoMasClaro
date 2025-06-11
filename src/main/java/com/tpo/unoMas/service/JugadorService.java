package com.tpo.unoMas.service;

import com.tpo.unoMas.dto.*;
import com.tpo.unoMas.model.*;
import com.tpo.unoMas.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * Servicio para gestión de jugadores
 * Implementa RF1: Registro de usuarios
 */
@Service
@Transactional
public class JugadorService {

    @Autowired
    private JugadorRepository jugadorRepository;
    
    @Autowired
    private ZonaRepository zonaRepository;
    
    @Autowired
    private DeporteRepository deporteRepository;
    
    @Autowired
    private PartidoRepository partidoRepository;

    /**
     * RF1: Registrar un nuevo jugador
     */
    public Jugador registrarJugador(RegistroJugadorRequest request) {
        // Validar que el email no exista
        if (jugadorRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Ya existe un jugador con ese email");
        }
        
        // Obtener zona y deporte
        Zona zona = zonaRepository.findById(request.getZonaId())
                .orElseThrow(() -> new RuntimeException("Zona no encontrada"));
        
        Deporte deporteFavorito = deporteRepository.findById(request.getDeporteFavoritoId())
                .orElseThrow(() -> new RuntimeException("Deporte no encontrado"));
        
        // Crear jugador
        Jugador jugador = new Jugador();
        jugador.setNombre(request.getNombre());
        jugador.setEmail(request.getEmail());
        jugador.setZona(zona);
        jugador.setNivel(request.getNivel());
        jugador.setTelefono(request.getTelefono());
        jugador.agregarAFavoritos(deporteFavorito);
        
        return jugadorRepository.save(jugador);
    }

    /**
     * Obtener jugador por ID
     */
    public Jugador obtenerPorId(Long id) {
        return jugadorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Jugador no encontrado"));
    }

    /**
     * Obtener jugador por email
     */
    public Jugador obtenerPorEmail(String email) {
        return jugadorRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Jugador no encontrado"));
    }

    /**
     * Obtener todos los jugadores
     */
    public List<Jugador> obtenerTodos() {
        return jugadorRepository.findAll();
    }

    /**
     * Obtener jugadores por zona
     */
    public List<Jugador> obtenerPorZona(Long zonaId) {
        return jugadorRepository.findByZonaId(zonaId);
    }

    /**
     * Actualizar jugador
     */
    public Jugador actualizarJugador(Long id, RegistroJugadorRequest request) {
        Jugador jugador = obtenerPorId(id);
        
        // Validar email si cambió
        if (!jugador.getEmail().equals(request.getEmail()) && 
            jugadorRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Ya existe un jugador con ese email");
        }
        
        // Obtener zona y deporte si cambiaron
        if (!jugador.getZona().getId().equals(request.getZonaId())) {
            Zona zona = zonaRepository.findById(request.getZonaId())
                    .orElseThrow(() -> new RuntimeException("Zona no encontrada"));
            jugador.setZona(zona);
        }

        /*
        if (!jugador.getDeporteFavorito().getId().equals(request.getDeporteFavoritoId())) {
            Deporte deporteFavorito = deporteRepository.findById(request.getDeporteFavoritoId())
                    .orElseThrow(() -> new RuntimeException("Deporte no encontrado"));
            jugador.setDeporteFavorito(deporteFavorito);
        }
         */
        
        // Actualizar campos
        jugador.setNombre(request.getNombre());
        jugador.setEmail(request.getEmail());
        jugador.setNivel(request.getNivel());
        jugador.setTelefono(request.getTelefono());
        
        return jugadorRepository.save(jugador);
    }

    /**
     * Obtener estadísticas del jugador
     */
    public Map<String, Object> obtenerEstadisticas(Long jugadorId) {
        Jugador jugador = obtenerPorId(jugadorId);
        
        // Obtener partidos donde participó
        List<Partido> partidosJugados = partidoRepository.findByJugadoresContaining(jugador);
        
        // Obtener partidos que organizó
        List<Partido> partidosOrganizados = partidoRepository.findByOrganizador(jugador);
        
        Map<String, Object> estadisticas = new HashMap<>();
        estadisticas.put("partidosJugados", partidosJugados.size());
        estadisticas.put("partidosOrganizados", partidosOrganizados.size());
        estadisticas.put("totalPartidos", partidosJugados.size() + partidosOrganizados.size());
        estadisticas.put("zona", jugador.getZona().getNombre());
        estadisticas.put("nivel", jugador.getNivel());
        
        return estadisticas;
    }

    /**
     * Convertir Jugador a DTO
     */
    public JugadorDTO convertirADTO(Jugador jugador) {
        JugadorDTO dto = new JugadorDTO();
        dto.setId(jugador.getId());
        dto.setNombre(jugador.getNombre());
        dto.setEmail(jugador.getEmail());
        dto.setZona(jugador.getZona().getNombre());
        dto.setNivel(jugador.getNivel());
        dto.setTelefono(jugador.getTelefono());
        
        // Agregar estadísticas
        List<Partido> partidosJugados = partidoRepository.findByJugadoresContaining(jugador);
        List<Partido> partidosOrganizados = partidoRepository.findByOrganizador(jugador);
        
        dto.setPartidosJugados(partidosJugados.size());
        dto.setPartidosOrganizados(partidosOrganizados.size());
        
        return dto;
    }

    /**
     * Obtener jugadores disponibles para un partido (excluyendo al organizador y sin solapamiento de horarios)
     */
    public List<Jugador> obtenerDisponiblesParaPartido(LocalDateTime fechaHora, int duracionMinutos, Long organizadorId) {
        List<Jugador> jugadores = jugadorRepository.findAll().stream()
            .filter(j -> !j.getId().equals(organizadorId))
            .collect(Collectors.toList());
        return jugadores.stream()
            .filter(j -> estaDisponible(j, fechaHora, duracionMinutos))
            .collect(Collectors.toList());
    }

    private boolean estaDisponible(Jugador jugador, LocalDateTime fechaHora, int duracionMinutos) {
        List<Partido> partidos = partidoRepository.findByJugadoresContaining(jugador);
        return partidos.stream().noneMatch(p -> seSolapan(p.getFechaHora(), p.getDuracionMinutos(), fechaHora, duracionMinutos));
    }

    private boolean seSolapan(LocalDateTime fecha1, int duracion1, LocalDateTime fecha2, int duracion2) {
        LocalDateTime fin1 = fecha1.plusMinutes(duracion1);
        LocalDateTime fin2 = fecha2.plusMinutes(duracion2);
        return !fecha1.isAfter(fin2) && !fecha2.isAfter(fin1);
    }
} 