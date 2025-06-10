package com.tpo.unoMas.model.strategy;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.tpo.unoMas.model.Notificacion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NotificacionPushFirebase implements INotificacionStrategy {
    private static final Logger logger = LoggerFactory.getLogger(NotificacionPushFirebase.class);
    private static final String ERROR_NOTIFICACION = "Error al enviar notificación push: {}";

    @Override
    public void enviarNotificacion(Notificacion notificacion) {
        String tokenFCM = notificacion.getDestinatario().getTokenFCM();
        if (tokenFCM != null && !tokenFCM.isEmpty()) {
            try {
                if (FirebaseApp.getApps().isEmpty()) {
                    logger.warn("Firebase no está disponible. No se enviará la notificación push.");
                    return;
                }

                Message message = Message.builder()
                        .setNotification(Notification.builder()
                                .setTitle(notificacion.getTitulo())
                                .setBody(notificacion.getMensaje())
                                .build())
                        .setToken(tokenFCM)
                        .build();

                FirebaseMessaging.getInstance().send(message);
                logger.info("Notificación push enviada exitosamente a {}", notificacion.getDestinatario().getEmail());
            } catch (Exception e) {
                logger.error(ERROR_NOTIFICACION, e.getMessage());
                throw new RuntimeException("Error al enviar notificación push", e);
            }
        }
    }
}