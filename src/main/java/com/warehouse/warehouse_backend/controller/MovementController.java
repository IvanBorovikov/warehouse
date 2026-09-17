package com.warehouse.warehouse_backend.controller;

import com.warehouse.warehouse_backend.aspect.annotation.Auditable;
import com.warehouse.warehouse_backend.dto.movement.MovementDTO;
import com.warehouse.warehouse_backend.dto.movement.MovementReadDTO;
import com.warehouse.warehouse_backend.repository.ProductRepository;
import com.warehouse.warehouse_backend.service.impl.MovementServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class MovementController {

    private final MovementServiceImpl movementService;

    @GetMapping("/movements/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<MovementReadDTO>> findMovementsById(@PathVariable("id") Long productId){

        return ResponseEntity.ok(movementService.getHistoryByProduct(productId));
    }

    @PostMapping("/incoming")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMMODITY_EXPERT')")
    @Auditable(action = "INCOMING")
    public ResponseEntity<?> incoming(@RequestBody MovementDTO movementDTO){

        return ResponseEntity.ok(movementService.incoming(movementDTO));
    }

    @PostMapping("/outgoing")
    @PreAuthorize("permitAll()")
    @Auditable(action = "OUTGOING")
    public ResponseEntity<?> outgoing(@RequestBody MovementDTO movementDTO){
        return ResponseEntity.ok(movementService.outgoing(movementDTO));
    }

}
