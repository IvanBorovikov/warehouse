package com.warehouse.warehouse_backend.enums;

public enum Role {
    ADMIN("Админ"),
    COMMODITY_EXPERT("Товаровед"),
    WAREHOUSE_WORKER("Сотрудник склада");


    private final String role;

    Role(String role){
        this.role =role;
    }

    public String getRole() {
        return role;
    }
}
