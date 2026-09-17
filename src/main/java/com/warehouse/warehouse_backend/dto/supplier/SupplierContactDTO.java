package com.warehouse.warehouse_backend.dto.supplier;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierContactDTO {

    private String name;

    private String contactPerson;

    private String phone;

    private String email;

    private String address;
}
