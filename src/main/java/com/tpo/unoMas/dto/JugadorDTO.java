package com.tpo.unoMas.dto;

import com.tpo.unoMas.model.Nivel;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO para transferir información de jugadores
 */
public class JugadorDTO {
    private Long id;
    private String nombre;
    private String email;
    private String zona;
    private String telefono;
    private ZonaDTO zonaDTO;
    private List<DeporteJugadorDTO> deportesFavoritos;

    // Constructor vacío
    public JugadorDTO() {}

    // Constructor completo
    public JugadorDTO(Long id, String nombre, String email, String zona,
                      ArrayList<DeporteJugadorDTO> deportes, ZonaDTO zonadto ,String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.zona = zona;
        this.telefono = telefono;
        this.deportesFavoritos = deportes;
        this.zonaDTO = zonadto;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getZona() { return zona; }
    public void setZona(String zona) { this.zona = zona; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public ZonaDTO getZonaDTO() { return zonaDTO; }
    public void setZonaDTO(ZonaDTO zonaDTO) { this.zonaDTO = zonaDTO; }

    public List<DeporteJugadorDTO> getDeportesFavoritos() { return deportesFavoritos; }
    public void setDeportesFavoritos(List<DeporteJugadorDTO> deportesFavoritos) { this.deportesFavoritos = deportesFavoritos; }

} 