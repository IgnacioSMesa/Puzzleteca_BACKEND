package com.ignacio_natalia.api.servicios;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Async
    public void enviarCodigoVerificacion(String emailDestino, String codigo) throws MessagingException, UnsupportedEncodingException {

        MimeMessage mensaje = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

        helper.setTo(emailDestino);
        helper.setSubject("Tu acceso a Puzzleteca");
        helper.setFrom("puzzletecasupport@gmail.com", "Puzzleteca");
        helper.setReplyTo("puzzletecasupport@gmail.com");

        String contenido =
                "<html>" +
                        "<head>" +
                        "<meta name='viewport' content='width=device-width, initial-scale=1.0'/>" +
                        "<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'/>" +
                        "</head>" +
                        "<body style='margin:0; padding:0; font-family:Arial, sans-serif; background-color:#f4f6f5;'>" +

                        "<table width='100%' cellpadding='0' cellspacing='0' style='background-color:#f4f6f5;'>" +
                        "<tr><td align='center' style='padding:20px 10px;'>" +

                        "<table width='100%' cellpadding='0' cellspacing='0' style='max-width:600px; background:white; border-radius:10px; overflow:hidden; box-shadow:0 2px 8px rgba(0,0,0,0.1);'>" +

                        "<tr><td style='background-color:#2e7d32; padding:25px 20px; text-align:center;'>" +
                        "<img src='cid:logoPuzzleteca' style='width:100%; max-width:180px; height:auto;'/>" +
                        "</td></tr>" +

                        "<tr><td style='padding:30px 25px; color:#333;'>" +
                        "<h2 style='color:#2e7d32; margin-top:0; font-size:22px;'>Código de verificación</h2>" +
                        "<p style='font-size:16px; line-height:1.5;'>Hola,</p>" +
                        "<p style='font-size:16px; line-height:1.5;'>Has solicitado cambiar tu contraseña en <b>Puzzleteca</b>.</p>" +

                        "<table width='100%' cellpadding='0' cellspacing='0'>" +
                        "<tr><td align='center' style='padding:25px 0;'>" +
                        "<span style='display:inline-block; background:#e8f5e9; color:#2e7d32; padding:15px 30px; font-size:28px; letter-spacing:6px; border-radius:8px; font-weight:bold;'>" +
                        codigo +
                        "</span>" +
                        "</td></tr>" +
                        "</table>" +

                        "<p style='font-size:16px; line-height:1.5;'>Este código expira en <b>10 minutos</b>.</p>" +
                        "<p style='color:#777; font-size:14px; line-height:1.5;'>Si no has solicitado este cambio, puedes ignorar este correo.</p>" +
                        "</td></tr>" +

                        "<tr><td style='background:#f1f1f1; padding:15px; text-align:center; font-size:12px; color:#777;'>" +
                        "© Puzzleteca" +
                        "</td></tr>" +

                        "</table>" +
                        "</td></tr>" +
                        "</table>" +

                        "</body></html>";

        helper.setText(contenido, true);

        ClassPathResource logo = new ClassPathResource("static/images/titulo.png");
        helper.addInline("logoPuzzleteca", logo);

        emailSender.send(mensaje);
    }
}
