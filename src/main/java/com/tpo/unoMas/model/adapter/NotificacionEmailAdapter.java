package com.tpo.unoMas.model.adapter;


import com.tpo.unoMas.model.Notificacion;

public interface NotificacionEmailAdapter {
    void enviarEmail(Notificacion notificacion);
}