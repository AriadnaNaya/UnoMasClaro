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

    @NotBlank(message = "La password no puede estar vacia")
    @Size(min = 8, max = 21, message = "Debe tener entre 8 y 21 caracteres")
    private String password;
    
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

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    @Override
    public String toString() {
        return "RegistroJugadorRequest{" +
                "nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", zonaId=" + zonaId +
                ", telefono='" + telefono + '\'' +
                '}';
    }
} 