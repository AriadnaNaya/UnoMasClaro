package com.tpo.unoMas.dto;

import com.tpo.unoMas.model.Nivel;

public class DeporteJugadorDTO {
    private String deporte;
    private Nivel nivel;
    private boolean favorito;

    public DeporteJugadorDTO(String deporte, Nivel nivel, boolean favorito) {
        this.deporte = deporte;
        this.nivel = nivel;
        this.favorito = favorito;
    }

    public String getDeporte() {
        return deporte;
    }

    public void setDeporte(String deporte) {
        this.deporte = deporte;
    }

    public Nivel getNivel() {
        return nivel;
    }

    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }

    public boolean isFavorito() {
        return favorito;
    }

    public void setFavorito(boolean favorito) {
        this.favorito = favorito;
    }
}
