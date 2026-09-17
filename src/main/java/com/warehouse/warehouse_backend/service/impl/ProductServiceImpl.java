package com.warehouse.warehouse_backend.service.impl;

import com.warehouse.warehouse_backend.article.ArticleGenerator;
import com.warehouse.warehouse_backend.dto.category.CategoryDTO;
import com.warehouse.warehouse_backend.dto.product.ProductCreateDTO;
import com.warehouse.warehouse_backend.dto.product.ProductReadDTO;
import com.warehouse.warehouse_backend.dto.product.ProductUpdateDTO;
import com.warehouse.warehouse_backend.exception.NotFoundException;
import com.warehouse.warehouse_backend.model.Category;
import com.warehouse.warehouse_backend.model.Product;
import com.warehouse.warehouse_backend.repository.CategoryRepository;
import com.warehouse.warehouse_backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class ProductServiceImpl {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ArticleGenerator articleGenerator;

    @Transactional
    public Page<ProductReadDTO> findAllProducts(Pageable pageable) {

        return productRepository.findAll(pageable).map(product ->
                new ProductReadDTO(product.getId(), product.getArticle(), product.getName(),
                        product.getPrice(), product.getQuantity(),
                        new CategoryDTO(product.getCategory().getId(), product.getCategory().getName(), product.getCategory().getDescription())));
    }

    public Page<ProductReadDTO> findByArticle(String article, Pageable pageable) {
        Page<Product> products;

        if (article == null || article.trim().isEmpty()) {
            products = productRepository.findAll(pageable);
        } else {
            products = productRepository.findByArticleContainingIgnoreCase(article.trim(), pageable);
        }

        return products.map(product -> new ProductReadDTO(product.getId(),
                product.getArticle(),
                product.getName(),
                product.getPrice(),
                product.getQuantity(),
                new CategoryDTO(
                        product.getCategory().getId(),
                        product.getCategory().getName(),
                        product.getCategory().getDescription()
                )
        ));
    }

    @Transactional
    public ProductReadDTO getProductById(Long id){
        Product product = productRepository.findById(id).orElseThrow(() -> new NotFoundException("Товар не найден"));

        return new ProductReadDTO(product.getId(), product.getArticle(), product.getName(), product.getPrice(), product.getQuantity(),
                new CategoryDTO(product.getCategory().getId(), product.getCategory().getName(), product.getCategory().getDescription()));
    }

    @Transactional
    public ProductReadDTO addProduct(ProductCreateDTO productCreateDTO) {

        Category category = categoryRepository.findById(productCreateDTO.getCategoryId()).orElseThrow(
                () -> new NotFoundException("Такой категории нет"));

        String article = productCreateDTO.getArticle();

        if (article == null || article.trim().isEmpty()) {
            if (productCreateDTO.getName() == null || productCreateDTO.getName().trim().isEmpty()) {
                throw new NotFoundException("Название товара обязательно, если артикль не указан");
            }
        } else {
            if (productRepository.existsByArticle(article)) {
                throw new NotFoundException("Артикул '" + article + "' уже существует!");
            }
        }

        Product product = new Product();
        product.setArticle(article);
        product.setName(productCreateDTO.getName());
        product.setPrice(productCreateDTO.getPrice());
        product.setQuantity(0);
        product.setCategory(category);
        product = productRepository.save(product);

        return createProductReadDTO(product);
    }

    @Transactional
    public ProductReadDTO updateProduct(Long id, ProductUpdateDTO updateDTO){
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Товар не найден"));

        if (updateDTO.getName() != null ){
            product.setName(updateDTO.getName());
        }

        if (updateDTO.getPrice() != null) {
            product.setPrice(updateDTO.getPrice());
        }

        if (updateDTO.getCategory() != null) {
            Category category = categoryRepository.findById(updateDTO.getCategory()).orElseThrow(() ->
                    new NotFoundException(""));
            product.setCategory(category);
        }

        product = productRepository.save(product);

        return createProductReadDTO(product);
    }

    @Transactional
    public void deleteProductById(Long id){
        if (!productRepository.existsById(id)){
            throw new NotFoundException("Товар не найден");
        }
        productRepository.deleteById(id);
    }

    private static ProductReadDTO createProductReadDTO(Product product) {
        ProductReadDTO productDTO = new ProductReadDTO();
        productDTO.setArticle(product.getArticle());
        productDTO.setName(product.getName());
        productDTO.setPrice(product.getPrice());
        productDTO.setQuantity(product.getQuantity());
        productDTO.setCategory(createCategoryDTO(product.getCategory()));
        return productDTO;
    }

    private static CategoryDTO createCategoryDTO(Category category) {
        return new CategoryDTO(category.getId(), category.getName(), category.getDescription());
    }

    public String generateArticle(String productName) {
        if (productName == null || productName.trim().isEmpty()) {
            throw new NotFoundException("Название товара не может быть пустым");
        }
        return articleGenerator.generateArticle(productName.trim());
    }
}
