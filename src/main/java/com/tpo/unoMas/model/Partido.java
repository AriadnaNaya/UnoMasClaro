package com.tpo.unoMas.model;

import com.tpo.unoMas.model.estado.EstadoPartido;
import com.tpo.unoMas.model.estado.NecesitamosJugadores;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.validation.constraints.*;

@Entity
@Table(name = "partidos")
public class Partido  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El título no puede estar vacío")
    private String titulo;

    @NotNull(message = "La fecha y hora no pueden ser nulas")
    @Future(message = "La fecha y hora deben ser futuras")
    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @NotNull(message = "La zona no puede ser nula")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zona_id", nullable = false)
    private Zona zona;

    @NotNull(message = "El deporte no puede ser nulo")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deporte_id", nullable = false)
    private Deporte deporte;

    @NotNull(message = "El nivel no puede ser nulo")
    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_id", nullable = false)
    private Nivel nivel;

    @ManyToMany
    @JoinTable(
            name = "partido_jugador",
            joinColumns = @JoinColumn(name = "partido_id"),
            inverseJoinColumns = @JoinColumn(name = "jugador_id")
    )
    private Set<Jugador> jugadores = new HashSet<>();

    @Transient
    private EstadoPartido estado;

    @NotNull(message = "El organizador no puede ser null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizador_id", nullable = false)
    private Jugador organizador;

    @Column(nullable = false)
    private Integer minJugadores;

    @Column(nullable = false)
    private Integer maxJugadores;

    @NotNull(message = "La duración no puede ser nula")
    @Min(value = 1, message = "La duración debe ser al menos 1 minuto")
    @Column(nullable = false)
    private Integer duracionMinutos;

    @ManyToMany
    @JoinTable(
            name = "partido_jugador_confirmado",
            joinColumns = @JoinColumn(name = "partido_id"),
            inverseJoinColumns = @JoinColumn(name = "jugador_id")
    )
    private Set<Jugador> jugadoresConfirmados = new HashSet<>();


    // Constructores
    public Partido() {
        this.jugadores = new HashSet<>();
        this.jugadoresConfirmados = new HashSet<>();
        this.estado = new NecesitamosJugadores();
    }

    public Partido(String nombre, String descripcion, LocalDateTime fechaHora,
                   Integer minJugadores, Integer maxJugadores, Zona zona) {
        this.fechaHora = fechaHora;
        this.minJugadores = minJugadores;
        this.maxJugadores = maxJugadores;
        this.zona = zona;
        this.jugadores = new HashSet<>();
        this.jugadoresConfirmados = new HashSet<>();
        this.estado = new NecesitamosJugadores();
    }


    // Service Partido
    public void agregarJugador(Jugador jugador) {
        Objects.requireNonNull(jugador, "El jugador no puede ser null");
        if (this.estado != null) {
            this.estado.agregarJugador(this, jugador);
        } else {
            throw new IllegalStateException("No se puede agregar jugador sin un estado definido");
        }
    }

    public void removerJugador(Jugador jugador) {
        Objects.requireNonNull(jugador, "El jugador no puede ser null");
        if (this.estado != null) {
            this.estado.removerJugador(this, jugador);
        } else {
            throw new IllegalStateException("No se puede remover jugador sin un estado definido");
        }
    }

    //State Partido

    public void iniciar() {
        if (this.estado != null) {
            this.estado.iniciarPartido(this);
        }
    }

    public void finalizar() {
        if (this.estado != null) {
            this.estado.finalizarPartido(this);
        }
    }

    public void cancelar() {
        if (this.estado != null) {
            this.estado.cancelarPartido(this);
        }
    }

    public boolean agregarJugadorInterno(Jugador jugador) {
        boolean agregado = jugadores.add(jugador);
        if (agregado) {
            jugador.agregarPartido(this);
        }
        return agregado;
    }

    public boolean removerJugadorInterno(Jugador jugador) {
        boolean removido = jugadores.remove(jugador);
        if (removido) {
            jugador.removerPartido(this);
            jugadoresConfirmados.remove(jugador);
        }
        return removido;
    }

    public void cambiarEstado(EstadoPartido nuevoEstado) {
        Objects.requireNonNull(nuevoEstado, "El nuevo estado no puede ser null");
        this.estado = nuevoEstado;

    }

    // Metodos Utiles - Partido
    public boolean estaCompleto() {
        return jugadores.size() == maxJugadores;
    }

    public boolean estaEnElFuturo() {
        return fechaHora.isAfter(LocalDateTime.now());
    }

    public void confirmarAsistencia(Jugador jugador) {
        if (!jugadores.contains(jugador))
            throw new IllegalArgumentException("El jugador no está en la lista");

        jugadoresConfirmados.add(jugador);

        if (jugadoresConfirmados.size() == jugadores.size()) {
            estado.confirmarPartido(this);
        }
    }


    //Getter and Setter - Partido

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Zona getZona() {
        return zona;
    }

    public void setZona(Zona zona) {
        this.zona = zona;
    }

    public Deporte getDeporte() {
        return deporte;
    }

    public void setDeporte(Deporte deporte) {
        this.deporte = deporte;
    }

    public Nivel getNivel() {
        return nivel;
    }

    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }

    public Set<Jugador> getJugadores() {
        return jugadores;
    }

    public void setJugadores(Set<Jugador> jugadores) {
        this.jugadores = jugadores;
    }

    public EstadoPartido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPartido estado) {
        this.estado = estado;
    }

    public Jugador getOrganizador() {
        return organizador;
    }

    public void setOrganizador(Jugador organizador) {
        this.organizador = organizador;
    }

    public Integer getMinJugadores() {
        return minJugadores;
    }

    public void setMinJugadores(Integer minJugadores) {
        this.minJugadores = minJugadores;
    }

    public Integer getMaxJugadores() {
        return maxJugadores;
    }

    public void setMaxJugadores(Integer maxJugadores) {
        this.maxJugadores = maxJugadores;
    }

    public Integer getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(Integer duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public Set<Jugador> getJugadoresConfirmados() {
        return jugadoresConfirmados;
    }

    public void setJugadoresConfirmados(Set<Jugador> jugadoresConfirmados) {
        this.jugadoresConfirmados = jugadoresConfirmados;
    }


    @Override
    public String toString() {
        return "Partido{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", fechaHora=" + fechaHora +
                ", zona=" + zona +
                ", deporte=" + deporte +
                ", nivel=" + nivel +
                ", maxJugadores=" + maxJugadores +
                ", jugadores=" + jugadores +
                ", estado=" + estado +
                '}';
    }
}