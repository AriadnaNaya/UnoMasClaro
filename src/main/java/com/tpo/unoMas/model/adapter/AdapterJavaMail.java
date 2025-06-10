package com.tpo.unoMas.model.adapter;


import com.tpo.unoMas.model.Notificacion;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.Properties;

@Component
public class AdapterJavaMail implements NotificacionEmailAdapter {
    private static final Logger logger = LoggerFactory.getLogger(AdapterJavaMail.class);
    private static final String ERROR_EMAIL = "Error al enviar email: {}";

    @Override
    public void enviarEmail(Notificacion notificacion) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465");

            Session session = Session.getDefaultInstance(props, null);
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("noreply@demo.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(notificacion.getDestinatario().getEmail()));
            message.setSubject(notificacion.getTitulo());
            message.setText(notificacion.getMensaje());

            Transport.send(message);
            logger.info("Email enviado exitosamente a {}", notificacion.getDestinatario().getEmail());
        } catch (MessagingException e) {
            logger.error(ERROR_EMAIL, e.getMessage());
            throw new RuntimeException("Error al enviar email", e);
        }
    }
}