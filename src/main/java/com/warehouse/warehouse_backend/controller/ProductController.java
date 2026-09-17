package com.warehouse.warehouse_backend.controller;

import com.warehouse.warehouse_backend.aspect.annotation.Auditable;
import com.warehouse.warehouse_backend.dto.product.ProductCreateDTO;
import com.warehouse.warehouse_backend.dto.product.ProductReadDTO;
import com.warehouse.warehouse_backend.dto.product.ProductUpdateDTO;
import com.warehouse.warehouse_backend.request.ArticleGenerationRequest;
import com.warehouse.warehouse_backend.service.impl.ProductServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductServiceImpl productService;

    @GetMapping("/products")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Page<ProductReadDTO>> findAllProducts(@PageableDefault(size = 15, sort = "id",
    direction = Sort.Direction.ASC)Pageable pageable){
        Page<ProductReadDTO> page = productService.findAllProducts(pageable);

        if (page.isEmpty()){
            throw new IllegalArgumentException("Товаров нет");
        }

        return ResponseEntity.ok(page);
    }

    @GetMapping("/search")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Page<ProductReadDTO>> findByArticle(@RequestParam(value = "article", required = false) String article,
            @PageableDefault(size = 15, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<ProductReadDTO> pageProductDto = productService.findByArticle(article, pageable);
        return ResponseEntity.ok(pageProductDto);
    }

    @GetMapping("/products/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ProductReadDTO> getProductById(@PathVariable("id") Long id){
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PostMapping("/products")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMMODITY_EXPERT')")
    @Auditable(action = "CREATE_PRODUCT")
    public ResponseEntity<ProductReadDTO> addProduct(@RequestBody ProductCreateDTO productCreateDTO){

        return ResponseEntity.ok(productService.addProduct(productCreateDTO));

    }

    @PostMapping("/generate-article")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMMODITY_EXPERT')")
    public ResponseEntity<?> generateArticle(@Valid @RequestBody ArticleGenerationRequest request) {
        String article = productService.generateArticle(request.getName());
        return ResponseEntity.ok(Map.of("article", article));
    }

    @PutMapping("/products/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMMODITY_EXPERT')")
    @Auditable(action = "UPDATE_PRODUCT")
    public ResponseEntity<?> updateProduct(@PathVariable("id") Long id, @RequestBody ProductUpdateDTO updateDTO){
        return ResponseEntity.ok(productService.updateProduct(id, updateDTO));
    }

    @DeleteMapping("/products/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMMODITY_EXPERT')")
    @Auditable(action = "DELETE_PRODUCT")
    public ResponseEntity<?> deleteProductById(@PathVariable("id") Long id){
        productService.deleteProductById(id);
        return ResponseEntity.noContent().build();
    }

}
