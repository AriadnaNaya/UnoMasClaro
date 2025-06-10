package com.tpo.unoMas.service;

import com.tpo.unoMas.model.Jugador;
import com.tpo.unoMas.model.Notificacion;
import com.tpo.unoMas.model.Partido;
import com.tpo.unoMas.model.strategy.emparejamiento.*;
import com.tpo.unoMas.observer.Observer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio para gestionar invitaciones a partidos usando estrategias de emparejamiento
 * Implementa el patrón Strategy para encontrar jugadores potenciales
 * Implementa el patrón Observer para enviar invitaciones automáticamente al crear partidos
 */
@Service
public class InvitacionService implements Observer {
    
    @Autowired
    private NotificacionService notificacionService;
    
    // Estrategia por defecto para invitaciones automáticas
    private EstrategiaEmparejamiento estrategiaDefecto = new EmparejamientoPorCercania();
    
    // Lista de jugadores registrados (en un sistema real vendría de un repositorio)
    private List<Jugador> jugadoresRegistrados = new ArrayList<>();
    
    /**
     * Observer method - Se ejecuta cuando cambia el estado del partido
     * Envía invitaciones automáticamente cuando se crea un nuevo partido
     */
    @Override
    public void update(Partido partido) {
        // Solo enviar invitaciones cuando se crea el partido (estado inicial)
        if ("NecesitamosJugadores".equals(partido.getEstado().getClass().getSimpleName())) {
            System.out.println("🎯 PARTIDO CREADO - Activando sistema de invitaciones automáticas");
            enviarInvitacionesAutomaticas(partido);
        }
    }
    
    /**
     * Envía invitaciones automáticamente usando la estrategia por defecto
     */
    private void enviarInvitacionesAutomaticas(Partido partido) {
        if (jugadoresRegistrados.isEmpty()) {
            System.out.println("⚠️ No hay jugadores registrados para enviar invitaciones");
            return;
        }
        
        System.out.println("📧 Enviando invitaciones automáticas con estrategia: " + 
                          estrategiaDefecto.getDescripcion());
        
        List<Jugador> invitados = enviarInvitaciones(partido, jugadoresRegistrados, estrategiaDefecto);
        
        System.out.println("✅ Invitaciones automáticas enviadas a " + invitados.size() + " jugadores");
    }
    
    /**
     * Envía invitaciones a jugadores potenciales usando la estrategia proporcionada
     * @param partido El partido que necesita jugadores
     * @param jugadoresRegistrados Lista de todos los jugadores registrados
     * @param estrategia La estrategia de emparejamiento a usar
     * @return Lista de jugadores a los que se enviaron invitaciones
     */
    public List<Jugador> enviarInvitaciones(Partido partido, List<Jugador> jugadoresRegistrados, EstrategiaEmparejamiento estrategia) {
        // Encontrar jugadores potenciales
        List<Jugador> jugadoresPotenciales = estrategia.encontrarJugadoresPotenciales(partido, jugadoresRegistrados);
        
        // Enviar invitaciones
        for (Jugador jugador : jugadoresPotenciales) {
            enviarInvitacionAJugador(jugador, partido, estrategia.getDescripcion());
        }
        
        System.out.println("📧 Invitaciones enviadas usando " + estrategia.getDescripcion() + 
                          " a " + jugadoresPotenciales.size() + " jugadores");
        
        return jugadoresPotenciales;
    }
    
    /**
     * Envía invitaciones usando todas las estrategias (para partidos prioritarios)
     */
    public List<Jugador> enviarInvitacionesConTodasEstrategias(Partido partido, List<Jugador> jugadoresRegistrados) {
        System.out.println("🎯 ENVIANDO INVITACIONES CON TODAS LAS ESTRATEGIAS");
        
        // Crear instancias de estrategias
        EstrategiaEmparejamiento estrategiaCercania = new EmparejamientoPorCercania();
        EstrategiaEmparejamiento estrategiaNivel = new EmparejamientoPorNivel();
        EstrategiaEmparejamiento estrategiaHistorial = new EmparejamientoPorHistorial();
        
        // Enviar con cada estrategia
        List<Jugador> invitadosCercania = enviarInvitaciones(partido, jugadoresRegistrados, estrategiaCercania);
        List<Jugador> invitadosNivel = enviarInvitaciones(partido, jugadoresRegistrados, estrategiaNivel);
        List<Jugador> invitadosHistorial = enviarInvitaciones(partido, jugadoresRegistrados, estrategiaHistorial);
        
        // Unir todas las listas sin duplicados
        List<Jugador> todosInvitados = new ArrayList<>(invitadosCercania.stream().distinct().toList());
        invitadosNivel.stream().filter(j -> !todosInvitados.contains(j)).forEach(todosInvitados::add);
        invitadosHistorial.stream().filter(j -> !todosInvitados.contains(j)).forEach(todosInvitados::add);
        
        System.out.println("📊 Total único de jugadores invitados: " + todosInvitados.size());
        
        return todosInvitados;
    }
    
    /**
     * Envía la invitación individual a un jugador
     */
    private void enviarInvitacionAJugador(Jugador jugador, Partido partido, String motivoInvitacion) {
        String titulo = "¡Invitación a partido de " + partido.getDeporte().getNombre() + "!";
        String mensaje = String.format(
            "Te invitamos a unirte al partido '%s' el %s. " +
            "Motivo de invitación: %s. " +
            "¡Esperamos que puedas acompañarnos!",
            partido.getTitulo(),
            partido.getFechaHora().toString(),
            motivoInvitacion
        );
        
        // Enviar notificación usando el sistema existente
        // Crear un partido temporal para usar el sistema de notificaciones
        notificacionService.notificarConTitulo(partido, titulo, mensaje);
        
        // Log para seguimiento
        System.out.println("📧 Invitación enviada a " + jugador.getNombre() + " para " + partido.getTitulo());
    }
    
    /**
     * Obtiene información sobre todas las estrategias disponibles
     */
    public String getInformacionEstrategias() {
        EstrategiaEmparejamiento estrategiaCercania = new EmparejamientoPorCercania();
        EstrategiaEmparejamiento estrategiaNivel = new EmparejamientoPorNivel();
        EstrategiaEmparejamiento estrategiaHistorial = new EmparejamientoPorHistorial();
        
        return String.format(
            "📋 ESTRATEGIAS DE EMPAREJAMIENTO DISPONIBLES:\n" +
            "🌍 CERCANIA: %s\n" +
            "⭐ NIVEL: %s\n" +
            "📊 HISTORIAL: %s",
            estrategiaCercania.getDescripcion(),
            estrategiaNivel.getDescripcion(),
            estrategiaHistorial.getDescripcion()
        );
    }
    
    /**
     * Configura la estrategia por defecto para invitaciones automáticas
     */
    public void setEstrategiaDefecto(EstrategiaEmparejamiento estrategia) {
        this.estrategiaDefecto = estrategia;
        System.out.println("✅ Estrategia por defecto cambiada a: " + estrategia.getDescripcion());
    }
    
    /**
     * Registra jugadores para el sistema de invitaciones (simulación)
     */
    public void registrarJugadores(List<Jugador> jugadores) {
        this.jugadoresRegistrados = new ArrayList<>(jugadores);
        System.out.println("✅ Registrados " + jugadores.size() + " jugadores para invitaciones");
    }
    
    /**
     * Obtiene la estrategia por defecto actual
     */
    public EstrategiaEmparejamiento getEstrategiaDefecto() {
        return estrategiaDefecto;
    }
} 