package com.warehouse.warehouse_backend.dto.product;

import com.warehouse.warehouse_backend.dto.category.CategoryDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductReadDTO {

    private Long id;

    private String article;

    private String name;

    private BigDecimal price;

    private int quantity;

    private CategoryDTO category;


}
