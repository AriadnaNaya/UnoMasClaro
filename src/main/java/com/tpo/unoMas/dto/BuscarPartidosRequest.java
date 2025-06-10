package com.tpo.unoMas.dto;

import com.tpo.unoMas.model.Nivel;
import java.time.LocalDateTime;

/**
 * DTO para request de búsqueda de partidos
 * RF2: Búsqueda de partidos
 */
public class BuscarPartidosRequest {
    
    private Long zonaId;
    private Long deporteId;
    private Nivel nivel;
    private LocalDateTime fechaDesde;
    private LocalDateTime fechaHasta;
    private Boolean soloConEspaciosDisponibles = true; // Solo partidos que necesiten jugadores
    private String estado; // Opcional: NecesitamosJugadores, PartidoArmado, etc.
    
    // Constructor vacío
    public BuscarPartidosRequest() {}

    // Getters y Setters
    public Long getZonaId() { return zonaId; }
    public void setZonaId(Long zonaId) { this.zonaId = zonaId; }

    public Long getDeporteId() { return deporteId; }
    public void setDeporteId(Long deporteId) { this.deporteId = deporteId; }

    public Nivel getNivel() { return nivel; }
    public void setNivel(Nivel nivel) { this.nivel = nivel; }

    public LocalDateTime getFechaDesde() { return fechaDesde; }
    public void setFechaDesde(LocalDateTime fechaDesde) { this.fechaDesde = fechaDesde; }

    public LocalDateTime getFechaHasta() { return fechaHasta; }
    public void setFechaHasta(LocalDateTime fechaHasta) { this.fechaHasta = fechaHasta; }

    public Boolean getSoloConEspaciosDisponibles() { return soloConEspaciosDisponibles; }
    public void setSoloConEspaciosDisponibles(Boolean soloConEspaciosDisponibles) { 
        this.soloConEspaciosDisponibles = soloConEspaciosDisponibles; 
    }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String toString() {
        return "BuscarPartidosRequest{" +
                "zonaId=" + zonaId +
                ", deporteId=" + deporteId +
                ", nivel=" + nivel +
                ", fechaDesde=" + fechaDesde +
                ", fechaHasta=" + fechaHasta +
                ", soloConEspaciosDisponibles=" + soloConEspaciosDisponibles +
                ", estado='" + estado + '\'' +
                '}';
    }
} 