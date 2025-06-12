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
    private NotificacionService notificacionService;

    public Partido guardarPartido(Partido partido, List<Jugador> disponibles) {
        partido.invitarJugadores(disponibles);
        notificacionService.notificarConTitulo(partido, "Partido creado", "Se ha creado un partido");
        return partidoRepository.save(partido);
    }

    public List<Partido> buscarPartidosCompatiblesParaJugador(Jugador jugador) {
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

    public PartidoDTO convertirADTO(Partido partido){
        PartidoDTO partidodto= partido.convertirADTO();
        return partidodto;
    }

} 