package com.warehouse.warehouse_backend.service.impl;

import com.warehouse.warehouse_backend.dto.movement.MovementDTO;
import com.warehouse.warehouse_backend.dto.movement.MovementReadDTO;
import com.warehouse.warehouse_backend.dto.supplier.SupplierDTO;
import com.warehouse.warehouse_backend.enums.MovementType;
import com.warehouse.warehouse_backend.exception.NotFoundException;
import com.warehouse.warehouse_backend.model.Movement;
import com.warehouse.warehouse_backend.model.Product;
import com.warehouse.warehouse_backend.model.Supplier;
import com.warehouse.warehouse_backend.repository.MovementRepository;
import com.warehouse.warehouse_backend.repository.ProductRepository;
import com.warehouse.warehouse_backend.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MovementServiceImpl {

    private final MovementRepository movementRepository;
    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;

    @Transactional
    public List<MovementReadDTO> getHistoryByProduct(Long productId){
        if (!productRepository.existsById(productId)) {
            throw new NotFoundException("Товар с ID " + productId + " не найден");
        }

        List<Movement> listAllMove = movementRepository.findByProductIdOrderByCreatedAtDesc(productId);
        List<MovementReadDTO> movementReadDTOS = new ArrayList<>();
        for (Movement movement : listAllMove){
            MovementReadDTO movementReadDTO = getMovementReadDTO(movement);
            movementReadDTOS.add(movementReadDTO);
        }

        return movementReadDTOS;
    }

    private static MovementReadDTO getMovementReadDTO(Movement movement) {
        MovementReadDTO movementReadDTO = new MovementReadDTO();
        movementReadDTO.setId(movement.getId());
        movementReadDTO.setProductId(movement.getProduct().getId());
        movementReadDTO.setType(movement.getType());
        if (movement.getSupplier() != null) {
            movementReadDTO.setSupplierId(new SupplierDTO(
                    movement.getSupplier().getId(),
                    movement.getSupplier().getName()
            ));
        } else {
            movementReadDTO.setSupplierId(null);
        }
        movementReadDTO.setComment(movement.getComment());
        movementReadDTO.setCreatedAt(movement.getCreatedAt());
        return movementReadDTO;
    }

    @Transactional
    public ResponseEntity<?> incoming(MovementDTO movementDTO){
        Product product = productRepository.findById(movementDTO.getProductId()).orElseThrow(()
                -> new NotFoundException("Товар с ID " + movementDTO.getProductId() + " не найден"));

        Supplier supplier = supplierRepository.findById(movementDTO.getSupplierId()).orElseThrow(()
                -> new NotFoundException("Поставщик с ID " + movementDTO.getSupplierId() + " не найден"));;

        Movement movement = new Movement();
        movement.setProduct(product);
        movement.setType(MovementType.INCOMING);
        movement.setQuantity(movementDTO.getQuantity());
        movement.setSupplier(supplier);
        movement.setComment(movementDTO.getComment());

        product.setQuantity(product.getQuantity() + movementDTO.getQuantity());
        productRepository.save(product);

        return ResponseEntity.ok(movementRepository.save(movement));
    }

    @Transactional
    public ResponseEntity<?> outgoing(MovementDTO movementDTO){
        Product product = productRepository.findById(movementDTO.getProductId()).orElseThrow(()
                -> new NotFoundException("Товар с ID " + movementDTO.getProductId() + " не найден"));;

        Supplier supplier = null;
        if (movementDTO.getSupplierId() != null) {
            supplier = supplierRepository.findById(movementDTO.getSupplierId())
                    .orElseThrow(() -> new NotFoundException("Поставщик с ID " + movementDTO.getSupplierId() + " не найден"));
        }

        Movement movement = new Movement();
        movement.setProduct(product);
        movement.setType(MovementType.OUTGOING);
        movement.setQuantity(movementDTO.getQuantity());
        movement.setSupplier(supplier);
        movement.setComment(movementDTO.getComment());

        if (product.getQuantity() < movementDTO.getQuantity()) {
            throw new NotFoundException(
                    "Недостаточно товара на складе! Доступно: " + product.getQuantity() +
                            ", запрошено: " + movementDTO.getQuantity()
            );
        }

        product.setQuantity(product.getQuantity() - movementDTO.getQuantity());
        productRepository.save(product);
        movementRepository.save(movement);
        return ResponseEntity.ok(movementRepository.save(movement));
    }



}
