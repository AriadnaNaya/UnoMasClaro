package com.tpo.unoMas.dto;

import com.tpo.unoMas.model.Nivel;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para transferir información de partidos
 */
public class PartidoDTO {
    private Long id;
    private String titulo;
    private LocalDateTime fechaHora;
    private String zona;
    private String deporte;
    private Nivel nivel;
    private String organizador;
    private Integer duracionMinutos;
    private String estado;
    private List<JugadorDTO> jugadores;
    private List<JugadorDTO> jugadoresConfirmados;
    private Integer jugadoresNecesarios;
    private ZonaDTO zonaDTO;
    private DeporteDTO deporteDTO;
    private JugadorDTO organizadorDTO;

    // Constructor vacío
    public PartidoDTO() {}

    // Constructor completo
    public PartidoDTO(Long id, String titulo, LocalDateTime fechaHora, String zona, 
                     String deporte, Nivel nivel, String organizador,Integer duracionMinutos, String estado) {
        this.id = id;
        this.titulo = titulo;
        this.fechaHora = fechaHora;
        this.zona = zona;
        this.deporte = deporte;
        this.nivel = nivel;
        this.organizador = organizador;
        this.duracionMinutos = duracionMinutos;
        this.estado = estado;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public String getZona() { return zona; }
    public void setZona(String zona) { this.zona = zona; }

    public String getDeporte() { return deporte; }
    public void setDeporte(String deporte) { this.deporte = deporte; }

    public Nivel getNivel() { return nivel; }
    public void setNivel(Nivel nivel) { this.nivel = nivel; }

    public String getOrganizador() { return organizador; }
    public void setOrganizador(String organizador) { this.organizador = organizador; }

    public Integer getDuracionMinutos() { return duracionMinutos; }
    public void setDuracionMinutos(Integer duracionMinutos) { this.duracionMinutos = duracionMinutos; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public List<JugadorDTO> getJugadores() { return jugadores; }
    public void setJugadores(List<JugadorDTO> jugadores) { this.jugadores = jugadores; }

    public List<JugadorDTO> getJugadoresConfirmados() { return jugadoresConfirmados; }
    public void setJugadoresConfirmados(List<JugadorDTO> jugadoresConfirmados) {
        this.jugadoresConfirmados = jugadoresConfirmados; 
    }

    public Integer getJugadoresNecesarios() { return jugadoresNecesarios; }
    public void setJugadoresNecesarios(Integer jugadoresNecesarios) { 
        this.jugadoresNecesarios = jugadoresNecesarios; 
    }

    public ZonaDTO getZonaDTO() { return zonaDTO; }
    public void setZonaDTO(ZonaDTO zonaDTO) { this.zonaDTO = zonaDTO; }

    public DeporteDTO getDeporteDTO() { return deporteDTO; }
    public void setDeporteDTO(DeporteDTO deporteDTO) { this.deporteDTO = deporteDTO; }

    public JugadorDTO getOrganizadorDTO() { return organizadorDTO; }
    public void setOrganizadorDTO(JugadorDTO organizadorDTO) { this.organizadorDTO = organizadorDTO; }
} 