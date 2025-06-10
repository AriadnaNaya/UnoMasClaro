package com.tpo.unoMas.dto;

import com.tpo.unoMas.model.Nivel;

/**
 * DTO para transferir información de jugadores
 */
public class JugadorDTO {
    private Long id;
    private String nombre;
    private String email;
    private String zona;
    private Nivel nivel;
    private String telefono;
    private Integer partidosJugados;
    private Integer partidosOrganizados;

    // Constructor vacío
    public JugadorDTO() {}

    // Constructor completo
    public JugadorDTO(Long id, String nombre, String email, String zona, 
                     String deporteFavorito, Nivel nivel, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.zona = zona;
        this.nivel = nivel;
        this.telefono = telefono;
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

    public Nivel getNivel() { return nivel; }
    public void setNivel(Nivel nivel) { this.nivel = nivel; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public Integer getPartidosJugados() { return partidosJugados; }
    public void setPartidosJugados(Integer partidosJugados) { this.partidosJugados = partidosJugados; }

    public Integer getPartidosOrganizados() { return partidosOrganizados; }
    public void setPartidosOrganizados(Integer partidosOrganizados) { this.partidosOrganizados = partidosOrganizados; }
} 