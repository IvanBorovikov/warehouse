package com.warehouse.warehouse_backend.aspect.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) // Можно вешать на методы
@Retention(RetentionPolicy.RUNTIME)
public @interface Auditable {
    String action();
}
