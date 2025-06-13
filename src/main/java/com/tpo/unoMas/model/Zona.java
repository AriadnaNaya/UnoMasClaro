package com.tpo.unoMas.model;


import com.tpo.unoMas.dto.ZonaDTO;
import jakarta.persistence.*;
@Entity
@Table(name = "zonas")
public class Zona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String barrio;

    @Column(nullable = false, length = 120)
    private String partido;

    @Embedded
    private Ubicacion ubicacion;


    // Constructor vacío requerido por JPA
    public Zona() {}

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

    public String getNombre() {
        return barrio + ", " + partido;
    }

    public ZonaDTO convertirADTO() {
        ZonaDTO dto = new ZonaDTO();
        dto.setId(this.id);
        dto.setBarrio(this.barrio);
        dto.setPartido(this.partido);
        if (this.ubicacion != null) {
            dto.setLatitud(this.ubicacion.getLatitud());
            dto.setLongitud(this.ubicacion.getLongitud());
        }
        return dto;
    }
}
