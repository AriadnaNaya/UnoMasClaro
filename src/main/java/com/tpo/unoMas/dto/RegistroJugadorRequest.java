package com.tpo.unoMas.dto;

import com.tpo.unoMas.model.Nivel;
import jakarta.validation.constraints.*;

/**
 * DTO para request de registro de jugadores
 * RF1: Registro de usuarios
 */
public class RegistroJugadorRequest {
    
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;
    
    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email debe tener un formato válido")
    private String email;
    
    @NotNull(message = "La zona no puede ser nula")
    private Long zonaId;
    
    @NotNull(message = "El deporte favorito no puede ser nulo")
    private Long deporteFavoritoId;
    
    @NotNull(message = "El nivel no puede ser nulo")
    private Nivel nivel;
    
    private String telefono;
    
    // Constructor vacío
    public RegistroJugadorRequest() {}

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Long getZonaId() { return zonaId; }
    public void setZonaId(Long zonaId) { this.zonaId = zonaId; }

    public Long getDeporteFavoritoId() { return deporteFavoritoId; }
    public void setDeporteFavoritoId(Long deporteFavoritoId) { this.deporteFavoritoId = deporteFavoritoId; }

    public Nivel getNivel() { return nivel; }
    public void setNivel(Nivel nivel) { this.nivel = nivel; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    @Override
    public String toString() {
        return "RegistroJugadorRequest{" +
                "nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", zonaId=" + zonaId +
                ", deporteFavoritoId=" + deporteFavoritoId +
                ", nivel=" + nivel +
                ", telefono='" + telefono + '\'' +
                '}';
    }
} 