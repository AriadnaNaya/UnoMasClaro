package com.tpo.unoMas.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "notificaciones")
public class Notificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String mensaje;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jugador_id", nullable = false)
    private Jugador destinatario;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "leida", nullable = false)
    private boolean leida;

    protected Notificacion() {
        this.fechaCreacion = LocalDateTime.now();
        this.leida = false;
    }

    public Notificacion(String titulo, String mensaje, Jugador destinatario) {
        this();
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.destinatario = destinatario;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public Jugador getDestinatario() {
        return destinatario;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public boolean isLeida() {
        return leida;
    }

    public void marcarComoLeida() {
        this.leida = true;
    }

    // Métodos para compatibilidad con el adapter
    public String getAsunto() {
        return titulo;
    }
}