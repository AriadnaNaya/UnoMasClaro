package com.tpo.unoMas.model.strategy;

import com.tpo.unoMas.model.Notificacion;

public interface INotificacionStrategy {
    void enviarNotificacion(Notificacion notificacion);
}