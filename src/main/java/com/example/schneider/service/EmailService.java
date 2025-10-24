package com.example.schneider.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender mailSender;
    
    public void sendRegistrationEmail(String toEmail, String username) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Registro Exitoso - Agenda Schneider");
        message.setText("Hola " + username + ",\n\n" +
                       "¡Tu registro ha sido exitoso!\n\n" +
                       "Ya puedes acceder a tu cuenta y comenzar a gestionar tus actividades.\n\n" +
                       "Saludos.\n");
        
        mailSender.send(message);
    }
}
