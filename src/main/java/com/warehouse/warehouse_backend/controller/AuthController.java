package com.warehouse.warehouse_backend.controller;

import com.warehouse.warehouse_backend.aspect.annotation.Auditable;
import com.warehouse.warehouse_backend.dto.token.JwtAuthenticationDto;
import com.warehouse.warehouse_backend.dto.token.RefreshTokenDto;
import com.warehouse.warehouse_backend.dto.token.UserCredentialsDto;
import com.warehouse.warehouse_backend.model.PasswordResetToken;
import com.warehouse.warehouse_backend.model.User;
import com.warehouse.warehouse_backend.service.impl.PasswordResetServiceImpl;
import com.warehouse.warehouse_backend.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final PasswordResetServiceImpl tokenReset;
    private final UserServiceImpl userService;
    private final SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationDto> login(@RequestBody UserCredentialsDto dto){
        try {
            JwtAuthenticationDto jwtAuthenticationDto = userService.signIn(dto);
            return ResponseEntity.ok(jwtAuthenticationDto);
        } catch (AuthenticationException e){
            throw new RuntimeException("Authentication failed " + e.getMessage());
        }
    }

    @PostMapping("/logout")
    @Auditable(action = "LOGOUT")
    public String performLogout(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        this.logoutHandler.logout(request, response, authentication);
        return "redirect:/login";
    }

    @PostMapping("/refresh")
    public JwtAuthenticationDto refresh(@RequestBody RefreshTokenDto dto) {
        try {
            return userService.refreshToken(dto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @PostMapping("/forgot-password")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        if (!userService.findEmailOfRefreshPassword(email)){
            return ResponseEntity.badRequest().body("Пользователь с таким email не найден");
        }

        String token = tokenReset.generateToken(email);


        String resetLink = "http://localhost:5173/reset-password?token=" + token;

        String subject = "Сброс пароля";
        String text = "Здравствуйте!\n\n" +
                "Вы запросили сброс пароля для вашей учетной записи.\n" +
                "Для создания нового пароля перейдите по ссылке:\n" +
                resetLink + "\n\n" +
                "Ссылка действительна в течение 15 минут.\n\n" +
                "Если вы не запрашивали сброс пароля, проигнорируйте это письмо.\n\n" +
                "С уважением,\n" +
                "Команда Складского учета";
        tokenReset.sendReferenceOfMail(email, subject, text);
        return ResponseEntity.ok("Инструкция отправлена на email");
    }

    // Сброс пароля
    @PostMapping("/reset-password")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        // 1. Проверить валидность токена

        if (tokenReset.validateToken(token)){
            tokenReset.resetPassword(token, newPassword);
            return ResponseEntity.ok("Пароль успешно изменен");
        }

        return ResponseEntity.badRequest().body("Неверный или истекший токен");
    }

}
