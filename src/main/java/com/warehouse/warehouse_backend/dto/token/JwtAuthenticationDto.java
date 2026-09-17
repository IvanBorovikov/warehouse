package com.warehouse.warehouse_backend.dto.token;

import com.warehouse.warehouse_backend.enums.Role;
import lombok.Data;

@Data
public class JwtAuthenticationDto {
    private String token;
    private String refreshToken;
    private Role role;
}
