package com.warehouse.warehouse_backend.controller;

import com.warehouse.warehouse_backend.dto.token.JwtAuthenticationDto;
import com.warehouse.warehouse_backend.dto.token.RefreshTokenDto;
import com.warehouse.warehouse_backend.dto.token.UserCredentialsDto;
import com.warehouse.warehouse_backend.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserServiceImpl userService;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationDto> login(@RequestBody UserCredentialsDto dto){
        try {
            JwtAuthenticationDto jwtAuthenticationDto = userService.signIn(dto);
            return ResponseEntity.ok(jwtAuthenticationDto);
        } catch (AuthenticationException e){
            throw new RuntimeException("Authentication failed " + e.getMessage());
        }
    }

    @PostMapping("/refresh")
    public JwtAuthenticationDto refresh(@RequestBody RefreshTokenDto dto) {
        try {
            return userService.refreshToken(dto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
