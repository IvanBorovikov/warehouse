package com.warehouse.warehouse_backend.enums;

public enum MovementType {
    INCOMING("Приход"),
    OUTGOING("Списание");

    private final String type;

    MovementType(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
