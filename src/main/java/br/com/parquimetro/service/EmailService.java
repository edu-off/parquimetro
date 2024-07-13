package br.com.parquimetro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    protected void sendSimpleMessage(SimpleMailMessage customMessage, String destinatario, String mensagem) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(customMessage.getFrom());
        message.setTo(destinatario);
        message.setSubject(customMessage.getSubject());
        message.setText(mensagem);
        emailSender.send(message);
    }

}
