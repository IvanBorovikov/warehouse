package com.warehouse.warehouse_backend.aspect;

import com.warehouse.warehouse_backend.aspect.annotation.Auditable;
import com.warehouse.warehouse_backend.enums.ActionType;
import com.warehouse.warehouse_backend.model.User;
import com.warehouse.warehouse_backend.repository.UserRepository;
import com.warehouse.warehouse_backend.service.impl.AuditLogServiceImpl;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Arrays;
@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspect {

    private final AuditLogServiceImpl auditLogService;
    private final UserRepository userRepository;

    @Around("@annotation(auditable)")
    public Object logAction(ProceedingJoinPoint joinPoint, Auditable auditable) throws Throwable {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = null;

        if (auth != null && auth.isAuthenticated()) {
            Object principal = auth.getPrincipal();
            System.out.println("principal: " + principal);
            System.out.println("class: " + principal.getClass().getName());

             if (principal instanceof UserDetails) {
                String username = ((UserDetails) principal).getUsername();
                user = userRepository.findByEmail(username).orElse(null);
                if (user == null) {
                    user = userRepository.findByUsername(username).orElse(null);
                }
            }

            if (user == null) {
                System.out.println("Пользователь не найден для principal: " + principal);
            }
        }

        if (user == null) {
            System.out.println("Логирование пропущено: пользователь не найден");
            return joinPoint.proceed();
        }

        String action = auditable.action();
        Object result = joinPoint.proceed();
        String details = buildDetails(joinPoint.getArgs());

        auditLogService.log(user.getId(), ActionType.valueOf(action), details, getClientIp());
        System.out.println("Лог сохранен для пользователя: " + user.getUsername());

        return result;
    }

    private String buildDetails(Object[] args) {
        return "Выполнено действие с параметрами: " + Arrays.toString(args);
    }

    private String getClientIp() {

        return "127.0.0.1";
    }
}
