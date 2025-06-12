package com.tpo.unoMas.model;

import jakarta.persistence.*;

public class Ubicacion {

    @Column(name = "latitud", nullable = false, precision = 10, scale = 7)
    private Double latitud;      // ejemplo:  -34.6036840

    @Column(name = "longitud", nullable = false, precision = 10, scale = 7)
    private Double longitud;     // ejemplo:  -58.3815591

    // Constructor vacío requerido por JPA
    public Ubicacion() {}

    public Ubicacion(Double latitud, Double longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }
}
