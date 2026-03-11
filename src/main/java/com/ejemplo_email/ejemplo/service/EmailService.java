package com.ejemplo_email.ejemplo.service;

import com.google.zxing.WriterException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private QRService qrService;

    public void sendEmail(String to,String subject,String content){

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        message.setFrom("garcetejulian3@gmail.com");
        emailSender.send(message);
    }

    public void sendEmailConQR(String to, String content, String codigo){

        try{
            byte[] qrBytes = qrService.generarQR(codigo);

            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject("Prueba de Envios de QR");
            helper.setFrom("garcetejulian3@gmail.com");

            helper.setText("""
            <h2>Tu código QR</h2>
            <p>%s</p>
            <img src='cid:qrImage'>
            """.formatted(content), true);

            helper.addInline("qrImage", new ByteArrayResource(qrBytes), "image/png");

            emailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Error enviando email", e);
        }
    }



}
