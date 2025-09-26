package com.example.authservice.infrastructure.mail;

import com.example.authservice.application.port.MailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

import java.time.Instant;

@Component
@Profile({"prod", "production"})
public class SMTPMailSender implements MailSender {
    private static final Logger log = LoggerFactory.getLogger(LogMailSender.class);
    Properties props = new Properties();
    String  SMTPhost = "sandbox.smtp.mailtrap.io";
    String  SMTPuser = "e48ab761ca6190";
    String  SMTPpassword = "e9248d3fbe40bd";

    private Session createSession() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTPhost);
        props.put("mail.smtp.port", "2525");
        props.put("mail.smtp.ssl.trust", SMTPhost);

        return Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTPuser, SMTPpassword);
            }
        });
    }

    private Message createMagicLinkMessage(Session session, String toEmail, String magicUrl, Instant expiresAt) 
        throws MessagingException {
        
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("example@example.com"));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
        message.setSubject("Seu Link de Acesso");
        message.setContent("Seu link de acesso: " + magicUrl + "\n\n" +
                           "Este link expira em: " + expiresAt.toString(), "text/plain; charset=utf-8");
        
        return message;
    }
    
    @Override
    public void sendMagicLink(String toEmail, String magicUrl, Instant expiresAt) {
        try {
            Session session = createSession();
            Message message = createMagicLinkMessage(session, toEmail, magicUrl, expiresAt);

            log.info("Enviando magic link para: {}", toEmail);
            Transport.send(message);
            log.info("Email enviado com sucesso!");
        } catch (MessagingException e) {
            log.error("Erro ao enviar magic link para {}: {}", toEmail, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
