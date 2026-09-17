package com.warehouse.warehouse_backend.controller;

import com.warehouse.warehouse_backend.aspect.annotation.Auditable;
import com.warehouse.warehouse_backend.dto.user.UserDTO;
import com.warehouse.warehouse_backend.dto.audit.AuditLogDTO;
import com.warehouse.warehouse_backend.dto.user.UserUpdateDTO;
import com.warehouse.warehouse_backend.service.impl.AuditLogServiceImpl;
import com.warehouse.warehouse_backend.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AdminController {

    private final UserServiceImpl userServiceImpl;
    private final AuditLogServiceImpl auditLogService;

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    @Auditable(action = "CREATE_USER")
    public ResponseEntity<UserDTO> register(@RequestBody @Valid UserDTO userDTO){
        return ResponseEntity.ok(userServiceImpl.registerUser(userDTO));
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserUpdateDTO>> findAllUsers(@PageableDefault(size = 5, sort = "id", direction = Sort.Direction.ASC)Pageable pageable){
        Page<UserUpdateDTO> allUsers = userServiceImpl.findAllUsers(pageable);
        return ResponseEntity.ok(allUsers);
    }

    @GetMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Optional<UserUpdateDTO>> findById(@PathVariable("id") Long id){
        Optional<UserUpdateDTO> user = userServiceImpl.findById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Auditable(action = "UPDATE_USER")
    public ResponseEntity<UserUpdateDTO> updateUser(@PathVariable("id")Long id, @RequestBody UserUpdateDTO userDTO){
        UserUpdateDTO update =  userServiceImpl.updateUser(id, userDTO);
        return ResponseEntity.ok(update);
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Auditable(action = "DELETE_USER")
    public ResponseEntity<String> deleteUserById(@PathVariable("id") Long id){
        userServiceImpl.deleteUser(id);

        return ResponseEntity.ok("Пользователь с id {" + id + "} удален");

    }


    @GetMapping("/users/audit")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<AuditLogDTO> findAudit(@PageableDefault(size = 15, sort = "id", direction = Sort.Direction.ASC)Pageable pageable){
        return auditLogService.findAudit(pageable);
    }
}
