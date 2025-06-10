package com.tpo.unoMas.model;

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

    @OneToMany(mappedBy = "deporte", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeporteJugador> jugadores = new ArrayList<>();

    @OneToMany(mappedBy = "deporte", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Partido> partidos = new ArrayList<>();

    // Constructor vacío requerido por JPA
    public Deporte() {}

    public Deporte(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
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

    public List<DeporteJugador> getJugadores() {
        return jugadores;
    }

    public void setJugadores(List<DeporteJugador> jugadores) {
        this.jugadores = jugadores;
    }

    public List<Partido> getPartidos() {
        return partidos;
    }

    public void setPartidos(List<Partido> partidos) {
        this.partidos = partidos;
    }

    @Override
    public String toString() {
        return "Deporte{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
