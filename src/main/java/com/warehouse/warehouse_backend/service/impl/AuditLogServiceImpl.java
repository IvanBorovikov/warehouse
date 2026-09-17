package com.warehouse.warehouse_backend.service.impl;

import com.warehouse.warehouse_backend.dto.audit.AuditLogDTO;
import com.warehouse.warehouse_backend.enums.ActionType;
import com.warehouse.warehouse_backend.model.AuditLog;
import com.warehouse.warehouse_backend.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AuditLogServiceImpl {

    private final AuditLogRepository auditLogRepository;

    @Transactional
    public Page<AuditLogDTO> findAudit(Pageable pageable){

        return auditLogRepository.findAll(pageable).map(auditLog ->
                new AuditLogDTO(auditLog.getUserId(), auditLog.getAction().name(), auditLog.getDetails(), auditLog.getCreatedAt()));
    }

    @Transactional
    public void log(Long userId, ActionType actionType, String details, String ip){
        AuditLog log = new AuditLog();
        log.setUserId(userId);
        log.setDetails(details);
        log.setAction(actionType);
        log.setIpAddress(ip);

        auditLogRepository.save(log);
    }
}
