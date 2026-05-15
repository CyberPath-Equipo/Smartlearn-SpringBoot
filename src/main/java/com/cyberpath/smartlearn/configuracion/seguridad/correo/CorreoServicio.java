package com.cyberpath.smartlearn.configuracion.seguridad.correo;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CorreoServicio {

    private final JavaMailSender mailSender;

    @Value("${smartlearn.mail.from:cyberpathcontacto@gmail.com}")
    private String from;

    public void enviarCodigoVerificacion(String destinatario, String nombreUsuario, String codigo) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(from);
            helper.setTo(destinatario);
            helper.setSubject("SmartLearn - Tu código de verificación");
            helper.setText(construirContenido(nombreUsuario, codigo), false);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new IllegalStateException("No fue posible enviar el correo de verificación", e);
        }
    }

    private String construirContenido(String nombreUsuario, String codigo) {
        String saludo = (nombreUsuario == null || nombreUsuario.isBlank()) ? "Hola" : "Hola, " + nombreUsuario;
        return saludo + "\n\n"
                + "Tu código de verificación de SmartLearn es: " + codigo + "\n\n"
                + "Este código expira en pocos minutos. Si no solicitaste este acceso, ignora este mensaje.";
    }
}

