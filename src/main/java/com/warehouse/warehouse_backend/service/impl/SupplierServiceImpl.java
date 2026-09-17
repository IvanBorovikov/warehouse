package com.warehouse.warehouse_backend.service.impl;

import com.warehouse.warehouse_backend.dto.supplier.SupplierContactDTO;
import com.warehouse.warehouse_backend.dto.supplier.SupplierCreateDTO;
import com.warehouse.warehouse_backend.dto.supplier.SupplierDTO;
import com.warehouse.warehouse_backend.exception.NotFoundException;
import com.warehouse.warehouse_backend.model.Supplier;
import com.warehouse.warehouse_backend.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SupplierServiceImpl {

    private final SupplierRepository supplierRepository;

    @Transactional
    public List<SupplierDTO> findAllSuppliers(){
        List<Supplier> listAll = supplierRepository.findAll();
        List<SupplierDTO> dtoList = new ArrayList<>();

        for (Supplier supplier : listAll){
            SupplierDTO dto = new SupplierDTO();
            dto.setId(supplier.getId());
            dto.setName(supplier.getName());
            dtoList.add(dto);
        }

        return dtoList;
    }

    public SupplierContactDTO createSupplier(SupplierCreateDTO supplierCreateDTO){
        if (supplierRepository.existsSupplierByEmail(supplierCreateDTO.getEmail())){
            throw new IllegalArgumentException("Такой пользователь уже существует");
        }

        Supplier supplier = new Supplier();
        supplier.setName(supplierCreateDTO.getName());
        supplier.setContactPerson(supplierCreateDTO.getContactPerson());
        supplier.setPhone(supplierCreateDTO.getPhone());
        supplier.setEmail(supplierCreateDTO.getEmail());
        supplier.setAddress(supplierCreateDTO.getAddress());

        supplierRepository.save(supplier);

        return new SupplierContactDTO(supplier.getName(),
                supplier.getContactPerson(),
                supplier.getPhone(),
                supplier.getEmail(),
                supplier.getAddress());
    }

    public SupplierContactDTO findSupplierById(Long id){
        Supplier supplier = supplierRepository.findById(id).orElseThrow(() ->
                new NotFoundException(""));

        return new SupplierContactDTO(supplier.getName(), supplier.getContactPerson(), supplier.getPhone(), supplier.getEmail(), supplier.getAddress());
    }

    @Transactional
    public SupplierContactDTO updateSupplier(Long id, SupplierContactDTO supplierContactDTO){
        Supplier supplier = supplierRepository.findById(id).orElseThrow(() ->
                new NotFoundException(""));

        if (supplierContactDTO.getName() != null || !supplierContactDTO.getName().equals(supplier.getName())){
            supplier.setName(supplierContactDTO.getName());
        }

        if (supplierContactDTO.getContactPerson() != null || !supplierContactDTO.getContactPerson().equals(supplier.getContactPerson())) {
            supplier.setContactPerson(supplierContactDTO.getContactPerson());
        }

        if (supplierContactDTO.getPhone() != null || !supplierContactDTO.getPhone().equals(supplier.getPhone())) {
            supplier.setPhone(supplierContactDTO.getPhone());
        }

        if (supplierContactDTO.getEmail() != null || !supplierContactDTO.equals(supplier.getEmail())) {
            supplier.setEmail(supplierContactDTO.getEmail());
        }

        if (supplierContactDTO.getAddress() != null || !supplierContactDTO.equals(supplier.getEmail())) {
            supplier.setAddress(supplierContactDTO.getAddress());
        }

        supplierRepository.save(supplier);

        return new SupplierContactDTO(supplier.getName(),supplier.getContactPerson(), supplier.getPhone(), supplier.getEmail(), supplier.getAddress());
    }


    public void deleteSupplierById(Long id){
        if (!supplierRepository.existsById(id)){
            throw new NotFoundException("Такого пользователя - нет");
        }
        supplierRepository.deleteById(id);
    }

}
