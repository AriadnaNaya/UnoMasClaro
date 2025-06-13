package com.tpo.unoMas.service;

import com.tpo.unoMas.model.Partido;
import com.tpo.unoMas.model.estado.*;
import com.tpo.unoMas.repository.PartidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio que maneja las transiciones automáticas de estado por tiempo
 * - Confirmado → EnJuego: cuando llega la fecha/hora del encuentro
 * - EnJuego → Finalizado: cuando termina el partido (fecha + duración)
 */
@Service
@Transactional
public class GestorEstadosPartidoService {

    @Autowired
    private PartidoRepository partidoRepository;

    @Scheduled(fixedRate = 60000)
    public void verificarTransicionesAutomaticas() {
        LocalDateTime ahora = LocalDateTime.now();
        
        // Transición: Confirmado → EnJuego
        iniciarPartidosConfirmados(ahora);
        
        // Transición: EnJuego → Finalizado  
        finalizarPartidosEnJuego(ahora);
    }

    private void iniciarPartidosConfirmados(LocalDateTime ahora) {
        List<Partido> partidos = partidoRepository.findAll();
        
        for (Partido partido : partidos) {
            // Solo partidos en estado Confirmado que ya llegaron a su hora
            if ("Confirmado".equals(partido.getEstadoDB()) && 
                partido.getFechaHora().isBefore(ahora.plusMinutes(1))) { // 1 minuto de tolerancia
                
                try {
                    partido.iniciar(); // Transición automática Confirmado → EnJuego
                    partidoRepository.save(partido);
                    
                    System.out.println("Partido iniciado automáticamente: " + partido.getTitulo());
                } catch (Exception e) {
                    System.err.println("Error al iniciar partido automáticamente: " + e.getMessage());
                }
            }
        }
    }

    private void finalizarPartidosEnJuego(LocalDateTime ahora) {
        List<Partido> partidos = partidoRepository.findAll();
        
        for (Partido partido : partidos) {
            // Solo partidos en estado EnJuego que ya terminaron
            if ("EnJuego".equals(partido.getEstadoDB())) {
                LocalDateTime horaFinalizacion = partido.getFechaHora()
                    .plusMinutes(partido.getDuracionMinutos());
                
                if (horaFinalizacion.isBefore(ahora)) {
                    try {
                        partido.finalizar(); // Transición automática EnJuego → Finalizado
                        partidoRepository.save(partido);
                        
                        System.out.println("Partido finalizado automáticamente: " + partido.getTitulo());
                    } catch (Exception e) {
                        System.err.println("Error al finalizar partido automáticamente: " + e.getMessage());
                    }
                }
            }
        }
    }

    public void verificarTransicionesManual() {
        verificarTransicionesAutomaticas();
    }
} 