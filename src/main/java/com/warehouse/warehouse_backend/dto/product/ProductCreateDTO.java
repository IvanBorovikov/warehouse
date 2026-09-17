package com.warehouse.warehouse_backend.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateDTO {

    @NotBlank(message = "Поле обязательное для заполнение")
    private String article;

    @NotBlank(message = "Название товара обязательно")
    private String name;

    @Positive(message = "Цена должна быть больше 0")
    private BigDecimal price;

    @NotNull(message = "Категория обязательна")
    private Long categoryId;


}
