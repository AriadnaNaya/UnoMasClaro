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

    @Autowired
    private DeporteJugadorRepository deporteJugadorRepository;

    public Jugador obtenerPorId(Long id) {
        return jugadorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Jugador no encontrado"));
    }

    public Jugador obtenerPorEmail(String email) {
        return jugadorRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Jugador no encontrado"));
    }

    public List<Jugador> obtenerTodos() {
        return jugadorRepository.findAll();
    }

    public List<Jugador> obtenerPorZona(Long zonaId) {
        return jugadorRepository.findByZonaId(zonaId);
    }

//  Update
    public Jugador actualizarJugador(Long id, RegistroJugadorRequest request) {
        Jugador jugador = obtenerPorId(id);

        if (!jugador.getEmail().equals(request.getEmail()) && 
            jugadorRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Ya existe un jugador con ese email");
        }

        if (!jugador.getZona().getId().equals(request.getZonaId())) {
            Zona zona = zonaRepository.findById(request.getZonaId())
                    .orElseThrow(() -> new RuntimeException("Zona no encontrada"));
            jugador.setZona(zona);
        }

        jugador.setNombre(request.getNombre());
        jugador.setEmail(request.getEmail());
        jugador.setTelefono(request.getTelefono());
        
        return jugadorRepository.save(jugador);
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
        dto.setTelefono(jugador.getTelefono());
        

        
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

    public Jugador guardar(Jugador jugador) {
        // Validar que el email no exista
        if (jugadorRepository.existsByEmail(jugador.getEmail())) {
            throw new RuntimeException("Ya existe un jugador con ese email");
        }
        return jugadorRepository.save(jugador);
    }

    /**
     * Agregar un deporte favorito al jugador
     */
    public Jugador agregarDeporteFavorito(Long jugadorId, Long deporteId, Nivel nivel) {
        Jugador jugador = obtenerPorId(jugadorId);
        Deporte deporte = deporteRepository.findById(deporteId)
                .orElseThrow(() -> new RuntimeException("Deporte no encontrado"));
        DeporteJugador dj = deporteJugadorRepository.findByJugadorAndDeporte(jugador, deporte);
        if (dj == null) {
            dj = new DeporteJugador(jugador, deporte, nivel, true);
            jugador.getDeportes().add(dj);
        } else if (dj.esFavorito()) {
            throw new RuntimeException("El deporte ya está marcado como favorito para este jugador");
        } else {
            dj.setEsFavorito(true);
            dj.setNivel(nivel);
        }
        deporteJugadorRepository.save(dj);
        return jugadorRepository.save(jugador);
    }

    /**
     * Eliminar un deporte de los favoritos del jugador
     */
    public Jugador eliminarDeporteFavorito(Long jugadorId, Long deporteId) {
        Jugador jugador = obtenerPorId(jugadorId);
        Deporte deporte = deporteRepository.findById(deporteId)
                .orElseThrow(() -> new RuntimeException("Deporte no encontrado"));
        DeporteJugador dj = deporteJugadorRepository.findByJugadorAndDeporte(jugador, deporte);
        if (dj != null) {
            dj.setEsFavorito(false);
            deporteJugadorRepository.save(dj);
        }
        return jugadorRepository.save(jugador);
    }

    /**
     * Actualizar el nivel de un deporte favorito del jugador
     */
    public Jugador actualizarNivelDeporte(Long jugadorId, Long deporteId, Nivel nivel) {
        Jugador jugador = obtenerPorId(jugadorId);
        Deporte deporte = deporteRepository.findById(deporteId)
                .orElseThrow(() -> new RuntimeException("Deporte no encontrado"));
        DeporteJugador dj = deporteJugadorRepository.findByJugadorAndDeporte(jugador, deporte);
        if (dj == null) {
            throw new RuntimeException("El jugador no tiene este deporte asociado");
        }
        dj.setNivel(nivel);
        deporteJugadorRepository.save(dj);
        return jugadorRepository.save(jugador);
    }
} 