package com.tpo.unoMas.dto;

import com.tpo.unoMas.model.Nivel;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

/**
 * DTO para request de creación de partidos
 * RF3: Creación de un partido
 */
public class CrearPartidoRequest {
    
    @NotBlank(message = "El título no puede estar vacío")
    private String titulo;
    
    @NotNull(message = "La fecha y hora no pueden ser nulas")
    @Future(message = "La fecha y hora deben ser futuras")
    private LocalDateTime fechaHora;
    
    @NotNull(message = "La zona no puede ser nula")
    private Long zonaId;
    
    @NotNull(message = "El deporte no puede ser nulo")
    private Long deporteId;
    
    @NotNull(message = "El nivel no puede ser nulo")
    private Nivel nivel;
    
    @NotNull(message = "El organizador no puede ser nulo")
    private Long organizadorId;
    
    @NotNull(message = "La duración no puede ser nula")
    @Min(value = 30, message = "Duración mínima 30 minutos")
    @Max(value = 300, message = "Duración máxima 300 minutos")
    private Integer duracionMinutos;
    
    // Estrategia de emparejamiento para invitaciones automáticas (opcional)
    private String estrategiaEmparejamiento = "CERCANIA"; // Por defecto cercanía

    // Constructor vacío
    public CrearPartidoRequest() {}

    // Getters y Setters
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public Long getZonaId() { return zonaId; }
    public void setZonaId(Long zonaId) { this.zonaId = zonaId; }

    public Long getDeporteId() { return deporteId; }
    public void setDeporteId(Long deporteId) { this.deporteId = deporteId; }

    public Nivel getNivel() { return nivel; }
    public void setNivel(Nivel nivel) { this.nivel = nivel; }

    public Long getOrganizadorId() { return organizadorId; }
    public void setOrganizadorId(Long organizadorId) { this.organizadorId = organizadorId; }

    public Integer getDuracionMinutos() { return duracionMinutos; }
    public void setDuracionMinutos(Integer duracionMinutos) { this.duracionMinutos = duracionMinutos; }

    public String getEstrategiaEmparejamiento() { return estrategiaEmparejamiento; }
    public void setEstrategiaEmparejamiento(String estrategiaEmparejamiento) { 
        this.estrategiaEmparejamiento = estrategiaEmparejamiento; 
    }

    @Override
    public String toString() {
        return "CrearPartidoRequest{" +
                "titulo='" + titulo + '\'' +
                ", fechaHora=" + fechaHora +
                ", zonaId=" + zonaId +
                ", deporteId=" + deporteId +
                ", nivel=" + nivel +
                ", duracionMinutos=" + duracionMinutos +
                ", estrategiaEmparejamiento='" + estrategiaEmparejamiento + '\'' +
                '}';
    }
} 