package com.warehouse.warehouse_backend.dto.supplier;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierCreateDTO {

    @NotNull(message = "Имя не должно быть пустым")
    private String name;

    private String contactPerson;

    @Pattern(regexp = "\\d - \\d{3}-\\d{3}-\\d{2}-\\d{2}",
            message = "Пример заполнения: 7-XXX-XXX-XXX-99")
    private String phone;

    @Email(message = "Некорректный формат email")
    private String email;

    private String address;
}
