package br.com.parquimetro.config;

import br.com.parquimetro.component.TextBuilderComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private Integer port;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Autowired
    private TextBuilderComponent textBuilder;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);
        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.debug", "true");
        return mailSender;
    }

    @Bean
    public SimpleMailMessage templateAlertMessageFixedTime() {
        String subject = textBuilder.subjectAlertMessageFixedTime();
        String message = textBuilder.textAlertMessageFixedTime();
        return getSimpleMailMessage(message, subject);
    }

    @Bean
    public SimpleMailMessage templateAlertMessageByHour() {
        String subject = textBuilder.subjectAlertMessageByHour();
        String message = textBuilder.textAlertMessageByHour();
        return getSimpleMailMessage(message, subject);
    }

    @Bean
    public SimpleMailMessage templatePaymentMessage() {
        String subject = textBuilder.subjectPaymentMessage();
        String message = textBuilder.textPaymentMessage();
        return getSimpleMailMessage(message, subject);
    }

    private SimpleMailMessage getSimpleMailMessage(String text, String subject) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@parquimetro.com.br");
        message.setSubject(subject);
        message.setText(text);
        return message;
    }

}
