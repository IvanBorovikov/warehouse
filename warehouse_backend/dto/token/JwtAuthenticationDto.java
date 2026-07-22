package com.warehouse.warehouse_backend.dto.token;

import lombok.Data;

@Data
public class JwtAuthenticationDto {
    private String token;
    private String refreshToken;
}
