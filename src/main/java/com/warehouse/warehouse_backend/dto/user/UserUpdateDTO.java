package com.warehouse.warehouse_backend.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.warehouse.warehouse_backend.enums.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDTO {

    private Long id;

    @NotBlank(message = "Username не может быть пустым")
    @Size(min = 8, max = 255, message = "Username должен быть от 8 до 255 символов")
    private String username;

    @NotBlank(message = "Password не может быть пустым")
    @Size(min = 6, max = 255, message = "Password должен быть от 6 до 255 символов")
    private String password;

    @NotBlank(message = "Email не может быть пустым")
    private String email;

    @NotBlank(message = "Full name не может быть пустым")
    private String fullName;

    @Enumerated(EnumType.STRING)
    private Role role;

    @CreationTimestamp
    @JsonIgnore
    private LocalDateTime created_at;
}
