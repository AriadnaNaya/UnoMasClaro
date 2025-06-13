package com.tpo.unoMas.model;


import jakarta.persistence.*;
@Entity
@Table(name = "zonas")
public class Zona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre del barrio (e.g. “Palermo”). */
    @Column(nullable = false, length = 120)
    private String barrio;

    /** Nombre del partido (e.g. “CABA” o “La Matanza”). */
    @Column(nullable = false, length = 120)
    private String partido;

    /** Coordenadas geográficas. */
    @Embedded
    private Ubicacion ubicacion;


    public Zona(String barrio, String partido, Ubicacion ubicacion) {
        this.barrio = barrio;
        this.partido = partido;
        this.ubicacion = ubicacion;
    }

    public Long getId() {
        return id;
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    public String getPartido() {
        return partido;
    }

    public void setPartido(String partido) {
        this.partido = partido;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }
}
