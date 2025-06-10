package com.tpo.unoMas.model.strategy.notificacion;

import com.tpo.unoMas.model.Notificacion;

public interface INotificacionStrategy {
    void enviarNotificacion(Notificacion notificacion);
}