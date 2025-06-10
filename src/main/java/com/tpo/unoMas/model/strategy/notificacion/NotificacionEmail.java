package com.tpo.unoMas.model.strategy.notificacion;

import com.tpo.unoMas.model.Notificacion;
import com.tpo.unoMas.model.adapter.NotificacionEmailAdapter;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Primary;

@Component
@Primary
public class NotificacionEmail implements INotificacionStrategy {
    private final NotificacionEmailAdapter emailAdapter;

    public NotificacionEmail(NotificacionEmailAdapter emailAdapter) {
        this.emailAdapter = emailAdapter;
    }

    @Override
    public void enviarNotificacion(Notificacion notificacion) {
        emailAdapter.enviarEmail(notificacion);
    }
}