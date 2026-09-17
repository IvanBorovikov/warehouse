package com.warehouse.warehouse_backend.controller;

import com.warehouse.warehouse_backend.aspect.annotation.Auditable;
import com.warehouse.warehouse_backend.dto.supplier.SupplierContactDTO;
import com.warehouse.warehouse_backend.dto.supplier.SupplierCreateDTO;
import com.warehouse.warehouse_backend.dto.supplier.SupplierDTO;
import com.warehouse.warehouse_backend.service.impl.SupplierServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class SupplierController {

    private final SupplierServiceImpl supplierService;

    @GetMapping("/suppliers")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<SupplierDTO>> findAllSuppliers(){
        return ResponseEntity.ok(supplierService.findAllSuppliers());
    }

    @PostMapping("/suppliers")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMMODITY_EXPERT')")
    @Auditable(action = "CREATE_SUPPLIER")
    public ResponseEntity<SupplierContactDTO> createSupplier(@RequestBody SupplierCreateDTO supplierCreateDTO){
        return ResponseEntity.ok(supplierService.createSupplier(supplierCreateDTO));
    }

    @GetMapping("/suppliers/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<SupplierContactDTO> findSupplierById(@PathVariable("id") Long id){
        return ResponseEntity.ok(supplierService.findSupplierById(id));
    }

    @PutMapping("/suppliers/{id}")
    @Auditable(action = "UPDATE_SUPPLIER")
    public ResponseEntity<?> updateSupplier(@PathVariable("id") Long id, @RequestBody SupplierContactDTO supplierContactDTO){
        return ResponseEntity.ok(supplierService.updateSupplier(id, supplierContactDTO));
    }

    @DeleteMapping("/suppliers/{id}")
    @Auditable(action = "DELETE_SUPPLIER")
    public ResponseEntity<?> deleteSupplier(@PathVariable("id") Long id) {
        supplierService.deleteSupplierById(id);
        return ResponseEntity.noContent().build();
    }
}
