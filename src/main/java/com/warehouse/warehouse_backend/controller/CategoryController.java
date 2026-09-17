package com.warehouse.warehouse_backend.controller;

import com.warehouse.warehouse_backend.aspect.annotation.Auditable;
import com.warehouse.warehouse_backend.dto.category.CategoryDTO;
import com.warehouse.warehouse_backend.dto.category.CategoryUpdateDTO;
import com.warehouse.warehouse_backend.service.impl.CategoryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class CategoryController {

    private final CategoryServiceImpl categoryService;

    @GetMapping("/categories")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<CategoryDTO>> findAllCategories(){

        return ResponseEntity.ok(categoryService.findAllCategories());
    }

    @PostMapping("/categories")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMMODITY_EXPERT')")
    @Auditable(action = "CREATE_CATEGORY")
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO){
        CategoryDTO created = categoryService.createCategory(categoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/categories/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<CategoryDTO> findCategoryById(@PathVariable("id") Long id){
        return ResponseEntity.ok(categoryService.findCategoryById(id));
    }

    @PutMapping("/categories/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMMODITY_EXPERT')")
    @Auditable(action = "UPDATE_CATEGORY")
    public ResponseEntity<?> updateCategory(@PathVariable("id") Long id, @RequestBody CategoryUpdateDTO categoryUpdateDTO){
        return ResponseEntity.ok(categoryService.updateCategory(id, categoryUpdateDTO));
    }

    @DeleteMapping("/categories/{id}")
    @Auditable(action = "DELETE_CATEGORY")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") Long id){
        categoryService.deleteCategoryById(id);
        return ResponseEntity.noContent().build();
    }

}
