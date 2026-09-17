package com.warehouse.warehouse_backend.service.impl;

import com.warehouse.warehouse_backend.exception.NotFoundException;
import com.warehouse.warehouse_backend.model.PasswordResetToken;
import com.warehouse.warehouse_backend.model.User;
import com.warehouse.warehouse_backend.repository.PasswordResetRepository;
import com.warehouse.warehouse_backend.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class PasswordResetServiceImpl {
    private final UserRepository userRepository;
    private final JavaMailSender mailSender;
    private final PasswordResetRepository repositoryToken;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public String generateToken(String email){
        repositoryToken.deleteByEmail(email);

        PasswordResetToken resetToken = new PasswordResetToken();
        String token = UUID.randomUUID().toString();
        resetToken.setToken(token);
        resetToken.setEmail(email);
        resetToken.setExpiredAt(LocalDateTime.now().plusMinutes(15));
        resetToken.setUsed(false);

        repositoryToken.save(resetToken);

        return token;
    }

    public void sendReferenceOfMail(String to, String subject, String text){

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true); // true = HTML
            helper.setFrom("borowikoviwan1901@gmail.com");
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Ошибка отправки email", e);
        }
    }

    @Transactional
    public boolean validateToken(String token){
        return repositoryToken.findByToken(token)
                .map(t -> !t.getUsed() && t.getExpiredAt().isAfter(LocalDateTime.now()))
                .orElse(false);
    }



    @Transactional
    public void resetPassword(String token,String newPassword){
        PasswordResetToken resetToken = repositoryToken.findByToken(token).orElseThrow(() -> new NotFoundException("Истек токен"));
        User user = userRepository.findByEmail(resetToken.getEmail()).orElseThrow(() -> new NotFoundException("Пользователь с таким email не найден"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        resetToken.setUsed(true);
        repositoryToken.save(resetToken);
    }




}
