package com.warehouse.warehouse_backend.dto.movement;

import com.warehouse.warehouse_backend.dto.supplier.SupplierDTO;
import com.warehouse.warehouse_backend.enums.MovementType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovementReadDTO {

    private Long id;

    private Long productId;

    private MovementType type;

    private Integer quantity;

    private SupplierDTO supplierId;

    private String comment;

    private LocalDateTime createdAt;
}
