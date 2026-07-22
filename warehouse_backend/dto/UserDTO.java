package com.warehouse.warehouse_backend.dto;

import com.warehouse.warehouse_backend.role.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDTO {

    @NotBlank(message = "Username не может быть пустым")
    @Size(min = 8, max = 255, message = "Username должен быть от 8 до 255 символов")
    private String username;

    @NotBlank(message = "Password не может быть пустым")
    @Size(min = 6, max = 255, message = "Password должен быть от 6 до 255 символов")
    private String password;

    @NotBlank(message = "Email не может быть пустым")
    private String email;

    @NotBlank(message = "Full name не может быть пустым")
    private String full_name;
    private Role role;
    private LocalDateTime created_at;
}
