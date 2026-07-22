package com.warehouse.warehouse_backend.dto.token;

import lombok.Data;

@Data
public class UserCredentialsDto {
    private String email;
    private String password;
}
