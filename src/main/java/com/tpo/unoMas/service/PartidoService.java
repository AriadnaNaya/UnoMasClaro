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

        //Usa la Strategy del partido para buscar jugadores que coincidan con la estrategia del partido.
        List<Jugador> jugadores = partido.matchearJugadores(disponibles);

        //Envia las notificaciones a todos los jugadores d eque se creeo un partido, invitandolos a sumarse
        notificacionService.notificarConTitulo(jugadores, "Partido creado", "Se ha creado un partido que te podria interesar");

        return partidoRepository.save(partido);
    }

    public List<Partido> buscarPartidosCompatiblesParaJugador(Jugador jugador) {

        //Busca los partidos en estado aceptable
        List<Partido> todosLosPartidos = partidoRepository.findPartidosActivos();

        //Para cada partido, se fija su estrategia y compara al jugador y al partido para ver si son compatibles
        return todosLosPartidos.stream()
            .filter(partido -> {
                EstrategiaEmparejamiento estrategia = partido.getEstrategiaEmparejamiento();
                if (estrategia == null) {
                    estrategia = new EmparejamientoPorCercania();
                }
                //Depende de cada estrategia que considera como compatible
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

//-------------------------  State --------------------------------------------------------

    //Utiliza el estado del partido para ver si un jugador puede unirse o no
    public void unirseAPartido(Long partidoId, Jugador jugador) {
        Partido partido = obtenerPorId(partidoId);
        partido.agregarJugador(jugador);
        partidoRepository.save(partido);
    }

    public void salirseDePartido(Long partidoId, Jugador jugador) {
        Partido partido = obtenerPorId(partidoId);
        partido.removerJugador(jugador);
        partidoRepository.save(partido);
    }

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

    //Cada jugador puede confirmar su asistencia.
    public void confirmarParticipacion(Long partidoId, Jugador jugador) {
        Partido partido = obtenerPorId(partidoId);
        if (!partido.getJugadores().contains(jugador)) {
            throw new RuntimeException("El jugador no está en este partido");
        }
        partido.confirmarAsistencia(jugador);
        partidoRepository.save(partido);
    }

//-------------------------  DTO --------------------------------------------------------
    public PartidoDTO convertirADTO(Partido partido){
        PartidoDTO partidodto= partido.convertirADTO();
        return partidodto;
    }

} 