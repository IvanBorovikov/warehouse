package com.warehouse.warehouse_backend.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ArticleGenerationRequest {
    @NotBlank(message = "Название товара не может быть пустым")
    private String name;

}
