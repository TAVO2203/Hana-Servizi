package com.example.hanaservizi_e.service;

import com.example.hanaservizi_e.model.User;
import com.example.hanaservizi_e.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final UserRepository userRepository; // tu repositorio

    public EmailService(JavaMailSender mailSender, UserRepository userRepository) {
        this.mailSender = mailSender;
        this.userRepository = userRepository;
    }

    public void enviarCorreoATodos(String asunto, String contenidoHtml) {
        List<User> usuarios = userRepository.findAll();

        for (User usuario : usuarios) {
            if (usuario.isActive()) { // solo usuarios activos
                try {
                    MimeMessage message = mailSender.createMimeMessage();
                    MimeMessageHelper helper = new MimeMessageHelper(message, true);

                    helper.setTo(usuario.getEmail());
                    helper.setSubject(asunto);
                    helper.setText(contenidoHtml, true); // true = HTML

                    mailSender.send(message);

                    System.out.println("✅ Correo enviado a: " + usuario.getEmail());

                    Thread.sleep(1000); // opcional: 1 seg entre envíos

                } catch (MessagingException e) {
                    System.err.println("❌ Error con " + usuario.getEmail() + ": " + e.getMessage());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
