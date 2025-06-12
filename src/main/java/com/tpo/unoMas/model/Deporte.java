package com.tpo.unoMas.model;

import com.tpo.unoMas.dto.DeporteDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "deportes")
public class Deporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    @Column(nullable = false, unique = true)
    private String nombre;

    @Size(max = 500, message = "La descripción no puede tener más de 500 caracteres")
    @Column(length = 500)
    private String descripcion;

    @Column(nullable = false)
    private int cantidadJugadores;

    // Constructor vacío requerido por JPA
    public Deporte() {}

    public Deporte(String nombre, String descripcion, int cantJugadores) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cantidadJugadores = cantJugadores;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCantidadJugadores() {return cantidadJugadores;}

    public DeporteDTO convertirADTO() {
        DeporteDTO dto = new DeporteDTO();
        dto.setId(this.id);
        dto.setNombre(this.nombre);
        dto.setDescripcion(this.descripcion);
        dto.setCantidadJugadores(this.cantidadJugadores);
        return dto;
    }

    @Override
    public String toString() {
        return "Deporte{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
