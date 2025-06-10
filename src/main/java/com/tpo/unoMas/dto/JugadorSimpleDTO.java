package com.tpo.unoMas.dto;

/**
 * DTO simple para transferir información básica de jugadores
 */
public class JugadorSimpleDTO {
    private Long id;
    private String nombre;
    private String email;
    private String zona;

    // Constructor vacío
    public JugadorSimpleDTO() {}

    // Constructor completo
    public JugadorSimpleDTO(Long id, String nombre, String email, String zona) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.zona = zona;
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
} 