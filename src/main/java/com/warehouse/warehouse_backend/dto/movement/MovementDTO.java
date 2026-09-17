package com.warehouse.warehouse_backend.dto.movement;

import com.warehouse.warehouse_backend.enums.MovementType;
import jakarta.persistence.Column;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovementDTO {

    private Long productId;

    private MovementType type;

    @PositiveOrZero(message = "Кол-во не может быть 0 и меньше")
    private Integer quantity;

    private Long supplierId;

    private String comment;

    private LocalDateTime created_at;
}
