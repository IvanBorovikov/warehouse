package com.warehouse.warehouse_backend.dto.audit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditLogDTO {
    private Long userId;
    private String action;
    private String details;
    private LocalDateTime createdAt;
}
